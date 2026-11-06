package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.UserRole;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserPutRequest {
  @NotEmpty(message = "username is required")
  private String username;
  @NotEmpty(message = "address is required")
  private String address;
  @NotEmpty(message = "firstname is required")
  private String firstname;
  @NotEmpty(message = "lastname is required")
  private String lastname;
  @NotEmpty(message = "name on receipt is required")
  private String nameOnReceipt;
  @NotNull(message = "role is required")
  private UserRole userRole;
}
