package com.project.webpos.Exception;

import lombok.Builder;
import lombok.Getter;

@Getter 
public class LimitReachedException extends RuntimeException {
  private String code;
  private String ipAddress;
  private String endpoint;
  private long availableToken;
  private long retryAfter;
  private int requestLimit;

  @Builder 
  public LimitReachedException(
    String code, String ipAddress, String endpoint, String message, long availableToken, long retryAfter, int requestLimit
  ){
    super(message);
    this.code = code;
    this.ipAddress = ipAddress;
    this.endpoint = endpoint;
    this.availableToken = availableToken;
    this.retryAfter = retryAfter;
    this.requestLimit = requestLimit;
  }
}
