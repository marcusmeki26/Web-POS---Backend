package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class InvalidRequestException extends RuntimeException{
  private String code;
  private String processOf;

  public InvalidRequestException(String code, String processOf, String message){
    super(message);
    this.code = code;
    this.processOf = processOf;
  }
}
