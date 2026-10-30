package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserTokenCreateRequest {
  @NotEmpty(message = "username is required")
  private String username;
  @NotEmpty(message = "password is required")
  private String password;
}
