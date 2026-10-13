package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class ConflictException extends RuntimeException {
  private String code;
  private String processOf;

  public ConflictException(String code, String processOf, String message){
    super(message);
    this.code = code;
    this.processOf = processOf;
  }
}
