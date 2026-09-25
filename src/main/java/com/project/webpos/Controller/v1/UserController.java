package com.project.webpos.Controller.v1;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.UserCreateRequest;
import com.project.webpos.Service.Security.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/v1/user")
public class UserController {
  private final AuthService authService;
  
  @PostMapping("/create")
  @PreAuthorize("hasRole('OWNER')")
  public ResponseEntity<Void> setNewUser(@Valid @RequestBody UserCreateRequest newUser){
    authService.createUser(newUser);
    return ResponseEntity.ok().build();
  }
}
