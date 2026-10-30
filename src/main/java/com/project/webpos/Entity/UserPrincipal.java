package com.project.webpos.Entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter 
public class UserPrincipal implements UserDetails {
    private final long id;
    private final String userId;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
 
    public UserPrincipal(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));
    }

    @JsonCreator
    public UserPrincipal(
        @JsonProperty("id") long id,
        @JsonProperty("userId") String userId,
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities
    ){
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public long getId(){
        return id;
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
