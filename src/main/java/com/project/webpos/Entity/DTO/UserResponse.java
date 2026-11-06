package com.project.webpos.Entity.DTO;

import com.project.webpos.Enum.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserResponse {
  private Integer id;
  private String username;
  private String address;
  private String firstname;
  private String lastname;
  private String nameOnReceipt;
  private UserRole userRole;
}
