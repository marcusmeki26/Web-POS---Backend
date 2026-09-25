package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserTokenCreateRequest {
  private String username;
  private String password;
  private String refreshToken;
}
