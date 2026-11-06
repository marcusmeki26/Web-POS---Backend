package com.project.webpos.Service;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.User;
import com.project.webpos.Entity.DTO.UserChangePasswordRequest;
import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Entity.DTO.UserPutRequest;
import com.project.webpos.Entity.DTO.UserResponse;
import com.project.webpos.Enum.UserRole;
import com.project.webpos.Exception.InvalidRequestException;
import com.project.webpos.Exception.ResourceNotFoundException;
import com.project.webpos.JWT.JWTCheckerPerRequest;
import com.project.webpos.Logging.LoggerFormat;
import com.project.webpos.Mapper.UserMapper;
import com.project.webpos.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

  /**
   * Fetches all non-deleted users
   * @return a list of {@code UserResponse}
   */
  public List<UserResponse> getUsers(){
    return userMapper.toResponseList(userRepository.findByIsDeletedIsFalse());
  }

  /**
   * Retrieves the values of {@code UserRole}
   * @return an array of the values defined on {@code UserRole}
   */
  public UserRole[] getUserRoles(){
    return UserRole.values();
  }

  /**
   * Performs a modification on a specific user
   * @param userPutRequest an object of {@code UserPutRequest} that contains the fields to be modified
   * @return an object of {@code UserResponse} that contains the newly updated fields/record
   */
  public UserResponse updateUser(Integer id, UserPutRequest userPutRequest){
    User userToUpdate = getUserById(id);
    userMapper.toPutUser(userPutRequest, userToUpdate);
    return userMapper.toResponse(userRepository.save(userToUpdate));
  }


  public void changePassword(Integer id, UserChangePasswordRequest userChangePasswordRequest){
    User user = getUserById(id);

    if(!encoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword()))
      throw new InvalidRequestException("INVALID_REQUEST", "changing of password", "The old password does not match");

    user.setPassword(encoder.encode(userChangePasswordRequest.getNewPassword()));

    userRepository.save(user);
  }

  /**
   * Register a new user. Can only be performed by admin/owner role
   * @param userCreateRequest value of {@code UserCreateRequest}
   */
  public void createUser(UserCreateRequest userCreateRequest){
    User savedUser = userRepository.save(userMapper.toPostUser(userCreateRequest, encoder));

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
   * Performs a soft deletion of a user
   * @param id the id of the user to perform soft deletion
   */
  public void deleteUser(Integer id){
    User userToDelete = getUserById(id);
    userToDelete.setIsDeleted(true);
    userRepository.save(userToDelete);
  }

  // =======================================================
  // UTILITIES
  // =======================================================
  /**
   * Performs a lookup on the {@code User} entity based on the specified id
   * @param id the id of the user to lookup
   * @return an object of {@code User}
   * @throws {@code ResourceNotFoundException} if there is no existing user
   */
  private User getUserById(Integer id){
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_EXIST", "The user does not exist"));
  }
}
