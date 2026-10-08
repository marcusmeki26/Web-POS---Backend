package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.UserRole;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserCreateRequest {
  @NotNull(message = "firstname is required")
  private String firstname;
  @NotNull(message = "lastname is required")
  private String lastname;
  @NotNull(message = "name on receipt is required")
  private String nameOnReceipt;
  @NotNull(message = "role is required")
  private UserRole userRole;
  @NotEmpty(message = "Username is required")
  private String username;
  @NotEmpty(message = "Password is required")
  private String password;
}
