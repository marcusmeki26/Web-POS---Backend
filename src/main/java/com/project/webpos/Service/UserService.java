package com.project.webpos.Service;

import org.springframework.stereotype.Service;

import com.project.webpos.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {
  private UserRepository userRepository;
}
