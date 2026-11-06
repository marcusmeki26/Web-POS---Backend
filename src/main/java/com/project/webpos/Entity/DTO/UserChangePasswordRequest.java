package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserChangePasswordRequest {
  @NotEmpty(message = "old password is required")
  private String oldPassword;
  @NotEmpty(message = "new password is required")
  private String newPassword;
}
