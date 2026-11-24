package com.project.webpos.Record;

public record LoginResponse(
  String accessToken,
  String refreshToken,
  UserInformation user
) {
  public record UserInformation(
    long id,
    String username
  ){
  }
}
