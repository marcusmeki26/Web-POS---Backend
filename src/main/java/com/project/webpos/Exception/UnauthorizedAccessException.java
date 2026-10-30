package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class UnauthorizedAccessException extends RuntimeException{
  private String code;

  public UnauthorizedAccessException(String code, String message){
    super(message);
    this.code = code;
  }
}
