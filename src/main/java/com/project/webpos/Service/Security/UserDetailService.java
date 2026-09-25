package com.project.webpos.Service.Security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.User;
import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Repository.UserRepository;

@Service
@RequiredArgsConstructor 
/**
 * =================================================================================================================
 * Spring automatically detects that this is the implementation of your UserDetailsService because of @Service
 * =================================================================================================================
 */
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
      User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("No existing user"));

      return new UserPrincipal(user);
    }
}