package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.UserRole;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserCreateRequest {
  private String firstname;
  private String lastname;
  private String nameOnReceipt;
  @NotNull(message = "A user should have a role")
  private UserRole userRole;
  @NotEmpty(message = "Username should not be blank")
  private String username;
  @NotEmpty(message = "Password should not be blank")
  private String password;
}
