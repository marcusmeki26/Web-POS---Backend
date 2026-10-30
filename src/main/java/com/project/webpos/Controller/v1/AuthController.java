package com.project.webpos.Controller.v1;


import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.AnnotationInterface.RateLimited;
import com.project.webpos.Entity.DTO.NewAccessKeyRequest;
import com.project.webpos.Entity.DTO.TokenResponse;
import com.project.webpos.Entity.DTO.UserLogoutRequest;
import com.project.webpos.Entity.DTO.UserTokenCreateRequest;
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
@Tag(name = "Authentication", description = "Endpoints for Authentication process")
public class AuthController {
  private final AuthService authService;

  @RateLimited
  @PostMapping("/api/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Handles User login", 
    description = "Performs validation of credentials. Returns the access key & refresh key if the credentials are valid"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User logged in successfully"),
    @ApiResponse(
      responseCode = "401", 
      description = "User has invalid credentails", 
      content = @Content(schema = @Schema(implementation = ErrorMessage.class))
    )
  })
  public TokenResponse login(
    @Valid @RequestBody UserTokenCreateRequest creds
  ){
    return authService.login(creds);
  }

  @PostMapping("/api/refresh")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Genates new access key",
    description = "Peforms refresh key validation. Returns a new access key if the refresh key is valid"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Access key created"),
    @ApiResponse(responseCode = "401", description = "Invalid refresh key", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public TokenResponse getAccessToken(
    @Valid @RequestBody NewAccessKeyRequest request
  ){
      return authService.verifyRefreshToken(request.getRefreshToken());
  }

  @PostMapping("/api/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Handles user logout",
    description = "Revokes all token that is associated with the user"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Log out successfully"),
    @ApiResponse(responseCode = "401", description = "No access token or invalid signature of access token", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public void logout(
    @RequestHeader("Authorization") String authHeader,
    @Valid @RequestBody UserLogoutRequest logoutRequest
  ){
    authService.logout(authHeader, logoutRequest);
  }
}
