package com.project.webpos.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j
public class GlobalExceptionHandler {
  // Exceptions thrown by Assert
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex){
    return new ResponseEntity<>(new ErrorMessage("INVALID_VALUE", ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }
  
  // Exceptions thrown during @Valid
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
    return new ResponseEntity<>(new ErrorMessage("INVALID_VALUE", ex.getBindingResult().getFieldError().getDefaultMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }

  // Exception thrown during login
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException ex){
    log.warn("Unable to perform login. Invalid username: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("INVALID_LOGIN", ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  // Exception thrown during login
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException ex){
    log.warn("Unable to perform login. Invalid password: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("INVALID_LOGIN", ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  // =======================================================
  // CUSTOM EXCEPTIONS
  // =======================================================
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex){
    log.warn("Resource not found: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex){
    log.warn("User not found: {} with a user id of {}", ex.getMessage(), ex.getUserId());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }

  @ExceptionHandler(InvalidCombinationException.class)
  public ResponseEntity<ErrorMessage> handleInvalidCombinationException(InvalidCombinationException ex){
    log.warn("Invalid combination of username and password: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }
  
  @ExceptionHandler(ExpiredTokenException.class)
  public ResponseEntity<ErrorMessage> handleExpiredTokenException(ExpiredTokenException ex){
    log.warn("Expired refresh token: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }
}
