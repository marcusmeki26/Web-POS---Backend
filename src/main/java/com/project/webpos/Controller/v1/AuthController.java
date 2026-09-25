package com.project.webpos.Controller.v1;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.UserTokenCreateRequest;
import com.project.webpos.Service.Security.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Map<String,String>> login(@RequestBody UserTokenCreateRequest creds){
    return ResponseEntity.ok(authService.login(creds));
  }

  @PostMapping("/refresh")
  public Map<String, String> getAccessToken(@RequestBody UserTokenCreateRequest userToken){
      return authService.verifyRefreshToken(userToken.getRefreshToken());
  }
}
