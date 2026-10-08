package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class InvalidCombinationException extends RuntimeException {
  private String code;

  public InvalidCombinationException(String code, String message){
    super(message);
    this.code = code;
  }
}
