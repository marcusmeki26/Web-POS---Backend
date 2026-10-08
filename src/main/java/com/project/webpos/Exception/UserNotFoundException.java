package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class UserNotFoundException extends RuntimeException {
  private String code;
  private String userId;

  public UserNotFoundException(String code, String message, String userId){
    super(message);
    this.code = code;
  }
}
