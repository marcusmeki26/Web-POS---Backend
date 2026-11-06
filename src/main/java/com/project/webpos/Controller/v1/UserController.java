package com.project.webpos.Controller.v1;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.UserChangePasswordRequest;
import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Entity.DTO.UserPutRequest;
import com.project.webpos.Entity.DTO.UserResponse;
import com.project.webpos.Enum.UserRole;
import com.project.webpos.Exception.ErrorMessage;
import com.project.webpos.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('OWNER')")
@Tag(name = "User", description = "Endpoints for managing user")
public class UserController {
  private final UserService userService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves all non-deleted user",
    description = "Fetches all non-deleted user"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Fetch all non-deleted user successfully"),
    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have the required permission to access this resouce", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public List<UserResponse> getUsers(){
    return userService.getUsers();
  }

  @GetMapping("/roles")
  @ResponseStatus(HttpStatus.OK)
  public UserRole[] getUserRoles(){
    return userService.getUserRoles();
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Modifies a user",
    description = "Modifies a user based on the given id"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User has been successfully updated"),
    @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content(schema = @Schema(implementation =  ErrorMessage.class)))
  })
  public UserResponse updateUser(
    @Parameter(description = "Id of the user to be modified", required = true, example = "10000")
    @PathVariable Integer id,
    @Valid @RequestBody UserPutRequest userPutRequest
  ){
    return userService.updateUser(id, userPutRequest);
  }

  @PutMapping("/change-password/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Modies the password of a specific user",
    description = "Modifies the password of a specific user based on the given request"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Password has been updated sucessfully"),
    @ApiResponse(responseCode = "422", description = "Old password does not match with the given request")
  })
  public void patchChangePassword(
    @Parameter(description = "Id of the user to be modified", required = true, example = "10000")
    @PathVariable Integer id,
    @Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest
  ){
    userService.changePassword(id, userChangePasswordRequest);
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
    summary = "Creates a new user",
    description = "Creates a new user within the system"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Added successfully"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public void setNewUser(
    @Valid @RequestBody UserCreateRequest newUser
  ){
    userService.createUser(newUser);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
    summary = "Soft deletes a user",
    description = "Soft deletes a user based on the specified id"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
    @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public void deleteUser(
    @Parameter(description = "Id of the user to be deleted", required = true, example = "10000")
    @PathVariable Integer id
  ){
    userService.deleteUser(id);
  }
}
