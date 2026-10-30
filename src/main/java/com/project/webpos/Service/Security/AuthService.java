package com.project.webpos.Service.Security;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.User;
import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Entity.DTO.TokenResponse;
import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Entity.DTO.UserLogoutRequest;
import com.project.webpos.Entity.DTO.UserTokenCreateRequest;
import com.project.webpos.Exception.InvalidCombinationException;
import com.project.webpos.Exception.UnauthorizedAccessException;
import com.project.webpos.Exception.UserNotFoundException;
import com.project.webpos.JWT.JWTCheckerPerRequest;
import com.project.webpos.JWT.JWTService;
import com.project.webpos.Logging.LoggerFormat;
import com.project.webpos.Repository.UserRepository;
import com.project.webpos.Service.Shared.CacheManagementService;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor  
@Slf4j
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JWTService jwtService;
  private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);
  private final CacheManagementService cacheManagementService;

  /**
   * Register a new user. Can only be performed by admin/owner role
   * @param newUser value of {@code UserCreateRequest}
   */
  public void createUser(UserCreateRequest newUser){
    Assert.notNull(newUser, "request not existing");
    Assert.hasText(newUser.getUsername(), "username is required");
    Assert.hasText(newUser.getPassword(), "password is required");
    Assert.notNull(newUser.getUserRole(), "role is required");

    User user = new User();
    user.setFirstname(newUser.getFirstname());
    user.setLastname(newUser.getLastname());
    user.setNameOnReceipt(newUser.getNameOnReceipt());
    user.setUserRole(newUser.getUserRole());
    user.setUsername(newUser.getUsername());
    user.setPassword(encoder.encode(newUser.getPassword()));
    User savedUser = userRepository.save(user);

    LoggerFormat loggerFormat = LoggerFormat.builder()
        .level("INFO")
        .action("log in user")
        .resourceId(savedUser.getUserId())
        .actorId(MDC.get(JWTCheckerPerRequest.USER_ID))
        .endpoint(MDC.get(JWTCheckerPerRequest.ENDPOINT))
        .build();

    log.atInfo()
      .setMessage("User added successfully")
      .addKeyValue("action_info", loggerFormat)
      .log();
  }

  /**
   * Authentication and generation of Json Web Token (JWT)
   * @param creds a value of {@code UserLoginCreateRequest}
   * @return an object of {@code TokenResponse} that contains access token & refresh token
   */
  public TokenResponse login(UserTokenCreateRequest creds){
    Assert.notNull(creds, "request not existing");
    Assert.hasText(creds.getUsername(), " username is required");
    Assert.hasText(creds.getPassword(), " password is required");

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
    ); // The authenticate method will call the AuthenticationProvider and verify if the user is valid. The Authentication object will receive the UserPrincipal if there is a user. Will go to UserDetailService

    if(authentication.isAuthenticated()){ // if the user exist and valid
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      
      LoggerFormat loggerFormat = LoggerFormat.builder()
        .level("INFO")
        .action("log in user")
        .resourceId(userPrincipal.getUserId())
        .actorId(userPrincipal.getUserId())
        .endpoint(MDC.get(JWTCheckerPerRequest.ENDPOINT))
        .build();
      
      log.atInfo()
        .setMessage("User logged in")
        .addKeyValue("action_info", loggerFormat)
        .log();
      return jwtService.generateToken(userPrincipal.getUsername(), userPrincipal.getUserId(), userPrincipal.getAuthorities()); // calling the method to generate the token.
    }

    throw new InvalidCombinationException("INVALID_USERNAME_AND_PASSWORD", "Username or password is incorrect");
  }

  /**
   * Generates a new access token and refresh token.
   * @param refreshToken a value of {@code String} that came from the user's request
   * @return an object of {@code TokenResponse} that has the access token and refresh token
   */
  public TokenResponse verifyRefreshToken(String refreshToken) {
    Assert.hasText(refreshToken, "refresh token is required");

    
    String userId = jwtService.validateRefreshToken(refreshToken); // fetches the user id first to avoid null exceptions
    User user = userRepository.findByUserId(userId);
    if(user == null)
      throw new UserNotFoundException("NO_EXISTING_USER", "User does not exist", userId);
    
    Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole() + ""));
    
    String jti = jwtService.extractRefreskTokenJti(refreshToken);
    
    String jtiCache = (String) cacheManagementService.getValueOfKey(jti);

    if(jtiCache != null)
      throw new UnauthorizedAccessException("BAD_REQUEST", "Using of old refresh token is prohibited");
    
    TokenResponse tokensResponse = jwtService.generateToken(user.getUsername(), user.getUserId(), authorities);
    
    cacheManagementService.addKey(jti, refreshToken, calculateTtlForJWT(jwtService.extractRefreshExpiration(refreshToken)));
    
    LoggerFormat loggerFormat = LoggerFormat.builder()
        .level("INFO")
        .action("access token generation")
        .resourceId(refreshToken)
        .actorId(user.getUserId())
        .endpoint(JWTCheckerPerRequest.ENDPOINT)
        .build();

    log.atInfo()
      .setMessage("Generated new access token")
      .addKeyValue("action_info", loggerFormat)
      .log();

    return tokensResponse;
  }

  /**
   * Performs a logout and revokes token inside Redis
   */
  public void logout(String authHeader, UserLogoutRequest logoutRequest){
    Assert.hasText(authHeader, "refresh token is required");

    if(authHeader != null && authHeader.startsWith("Bearer ")){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

      String token = authHeader.substring(7);
      if(jwtService.validateToken(token, loggedUser)){
        String accessKeyJti = jwtService.extractJti(token);  
        cacheManagementService.addKey(accessKeyJti, accessKeyJti, null);
        String refreshKeyJti = jwtService.extractRefreskTokenJti(logoutRequest.getRefreshToken());
        cacheManagementService.addKey(refreshKeyJti, refreshKeyJti, null);
        return;
      }else{
        throw new UnauthorizedAccessException("BAD_REQUEST", "Unable to perform logout");
      }
    }
    throw new UnauthorizedAccessException("BAD_REQUEST", "Unable to perform logout");
  }

  // =======================================================
  // UTILITIES
  // =======================================================
  /**
   * Calculates the ttl for the cache based on the expiry date of the token
   * @param tokenExpiryDate a {@code Date} value of the expiration date
   * @return a {@code Duration} value in minutes
   */
  private Duration calculateTtlForJWT(Date tokenExpiryDate){
    Date expirationDate = tokenExpiryDate;
    long differenceInMs = expirationDate.getTime() - new Date().getTime();
    return Duration.ofMinutes(differenceInMs / (1000 * 60)); // returns the value in a form of minutes 
  }
}
