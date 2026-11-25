package com.project.webpos.Exception;

import lombok.Getter;

@Getter 
public class StockIsEmptyException extends RuntimeException {
  private String code;

  public StockIsEmptyException(String code, String message){
    super(message);
    this.code = code;
  }
}
