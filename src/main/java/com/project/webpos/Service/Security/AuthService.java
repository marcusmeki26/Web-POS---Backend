package com.project.webpos.Service.Security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.User;
import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Entity.DTO.UserTokenCreateRequest;
import com.project.webpos.Exception.InvalidCombinationException;
import com.project.webpos.Exception.UserNotFoundException;
import com.project.webpos.Repository.UserRepository;

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
    userRepository.save(user);
  }

  /**
   * Authentication and generation of Json Web Token (JWT)
   * @param creds a value of {@code UserLoginCreateRequest}
   * @return {@code Map<String,String>} that contains access token & refresh token
   */
  public Map<String,String> login(UserTokenCreateRequest creds){
    Assert.notNull(creds, "request not existing");
    Assert.hasText(creds.getUsername(), " username is required");
    Assert.hasText(creds.getPassword(), " password is required");

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
    ); // The authenticate method will call the AuthenticationProvider and verify if the user is valid. The Authentication object will receive the UserPrincipal if there is a user. Will go to UserDetailService

    if(authentication.isAuthenticated()){ // if the user exist and valid
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("User {} logged in", userPrincipal.getUserId());
        return jwtService.generateToken(userPrincipal.getUsername(), userPrincipal.getUserId(), userPrincipal.getAuthorities()); // calling the method to generate the token.
    }

    throw new InvalidCombinationException("INVALID_USERNAME_AND_PASSWORD", "Username or password is incorrect");
    // return Map.of("Failed", "Wrong User");
  }

  public Map<String, String> verifyRefreshToken(String refreshToken) {
    Assert.hasText(refreshToken, "missing refresh token value");

    String userId = jwtService.validateRefreshToken(refreshToken); // fetches the user id first to avoid null exceptions
    User user = userRepository.findByUserId(userId);
    if(user == null)
        throw new UserNotFoundException("NO_EXISTING_USER", "User does not exist", userId);

    // Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole()));
    Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole() + ""));
    log.info("New access token is created to User {} with a refresh token of {}", user.getUserId(), refreshToken);
    return Map.of("access_token", jwtService.createAccessToken(user.getUsername(), authorities));
  }
}
