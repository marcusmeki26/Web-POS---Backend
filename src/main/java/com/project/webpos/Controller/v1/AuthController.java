package com.project.webpos.Controller.v1;


import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.AnnotationInterface.RateLimited;
import com.project.webpos.Entity.DTO.TokenResponse;
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
  @PostMapping("/login")
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

  @PostMapping("/refresh")
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
    @Valid @RequestBody UserTokenCreateRequest userToken
  ){
      return authService.verifyRefreshToken(userToken.getRefreshToken());
  }
}
