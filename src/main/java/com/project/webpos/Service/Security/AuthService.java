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
import com.project.webpos.Enum.UserRole;
import com.project.webpos.Repository.UserRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
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
    Assert.notNull(newUser, "User should not be null");
    Assert.notNull(newUser.getUsername(), "Username should not be null");
    Assert.notNull(newUser.getPassword(), "Password should not be null");
    Assert.notNull(newUser.getUserRole(), "A user should have a role");

    User user = new User();
    user.setFirstname(newUser.getFirstname());
    user.setLastname(newUser.getLastname());
    user.setNameOnReceipt(newUser.getNameOnReceipt());
    user.setUserRole(newUser.getUserRole());
    user.setUsername(newUser.getUsername());
    user.setPassword(encoder.encode(newUser.getPassword()));
    userRepository.save(user);
  }

  // =================================================================================================================
  // Creates a temporary user that has an owner role
  // =================================================================================================================
  public void createUserTemp(){
    // Creates a temp owner user for adding cashier and manager.
    // Move to user service since no authentication like JWT is needed.
    User user = new User();
    user.setFirstname("Marcus");
    user.setLastname("Felixmeña");
    user.setUserRole(UserRole.OWNER);
    user.setUsername("marcus23");
    user.setPassword(encoder.encode("nullsir"));
    userRepository.save(user);
  }

  /**
   * Authentication and generation of Json Web Token (JWT)
   * @param creds a value of {@code UserLoginCreateRequest}
   * @return {@code Map<String,String>} that contains access token & refresh token
   */
  public Map<String,String> login(UserTokenCreateRequest creds){
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
    ); // The authenticate method will call the AuthenticationProvider and verify if the user is valid. The Authentication object will receive the UserPrincipal if there is a user. Will go to UserDetailService

    if(authentication.isAuthenticated()){ // if the user exist and valid
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(userPrincipal.getUsername(), userPrincipal.getUserId(), userPrincipal.getAuthorities()); // calling the method to generate the token.
    }

    return Map.of("Failed", "Wrong User");
  }

  public Map<String, String> verifyRefreshToken(String refreshToken) {
      String userId = jwtService.validateRefreshToken(refreshToken); // fetches the user id first to avoid null exceptions

      // checks if the userId is null
      if(userId == null)
          return Map.of("Failed", "Invalid Refresh Token");

      User user = userRepository.findByUserId(userId);
      if(user == null)
          return Map.of("Failed", "No Users Found");

      Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole().getCaption()));
      return Map.of("access_token", jwtService.createAccessToken(user.getUsername(), authorities));
  }
}
