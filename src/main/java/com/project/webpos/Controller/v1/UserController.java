package com.project.webpos.Controller.v1;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Exception.ErrorMessage;
import com.project.webpos.Service.Security.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/v1/user")
@Tag(name = "User", description = "Endpoints for managing user")
public class UserController {
  private final AuthService authService;
  
  @PostMapping("/create")
  @PreAuthorize("hasRole('OWNER')")
  @Operation(
    summary = "Creates a new user",
    description = "Creates a new user within the system"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Added successfully"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> setNewUser(
    @Valid @RequestBody UserCreateRequest newUser
  ){
    authService.createUser(newUser);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/create/dummy")
  @Operation(
    summary = "This is a temporary endpoint for creating user",
    description = "Temporary endpoint to create a user indentity incase of accidental deletion of user"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Added successfully"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> dummyPostUser(
    @Valid @RequestBody UserCreateRequest postRequest
  ){
    authService.createUser(postRequest);
    return ResponseEntity.ok().build();
  }
}
