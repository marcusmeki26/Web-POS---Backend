package com.project.webpos.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor 
@Getter 
@Setter 
public class ErrorMessage {
  private String code;
  private String message;
}
