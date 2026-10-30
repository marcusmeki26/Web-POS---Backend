package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter 
public class UserLogoutRequest {
  @NotEmpty(message = "refresh token is required")
  private String refreshToken;
}
