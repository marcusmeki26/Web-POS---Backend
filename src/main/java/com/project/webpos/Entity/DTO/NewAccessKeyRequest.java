package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class NewAccessKeyRequest {
  @NotEmpty(message = "refresh key is required")
  private String refreshToken;
}
