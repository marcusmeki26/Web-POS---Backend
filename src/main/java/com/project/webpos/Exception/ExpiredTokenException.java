package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class ExpiredTokenException extends RuntimeException{
  private String code;

  public ExpiredTokenException(String code, String message){
    super(message);
    this.code = code;
  }
}
