package com.project.webpos.Exception;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j
public class GlobalExceptionHandler {
  // Exceptions thrown by Assert
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex){
    log.warn("Invalid value before data processing: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("INVALID_VALUE", ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }
  
  // Exceptions thrown during @Valid
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
    log.warn("Invalid value on request: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("INVALID_VALUE", ex.getBindingResult().getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
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

  // Exception thrown during SQL constraint violation
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
    log.warn("Unable to perform SQL query: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("INVALID_VALUE", ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<ErrorMessage> handleSignatureException(SignatureException ex){
    log.warn("Invalid access token signature was trying to access", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage("UNAUTHORIZED ACCESS", "You have an invalid credentails"), HttpStatus.UNAUTHORIZED);
  }

  // =======================================================
  // CUSTOM EXCEPTIONS
  // =======================================================
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex){
    log.warn("Resource not found: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex){
    log.warn("User not found: {} with a user id of {}", ex.getMessage(), ex.getUserId());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
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

  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<ErrorMessage> handleUnauthorizedAccessException(UnauthorizedAccessException ex){
    log.warn("A user requested an access but has invalid details", ex.getMessage());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorMessage> handleConflictException(ConflictException ex){
    log.warn("Conflicting values: {} during {}", ex.getMessage(), ex.getProcessOf());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorMessage> handleInvalidRequestException(InvalidRequestException ex){
    log.warn("Invalid request: {} during {}", ex.getMessage(), ex.getProcessOf());
    return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
  }

  @ExceptionHandler(LimitReachedException.class)
  public ResponseEntity<ErrorMessage> handleLimitReachedException(LimitReachedException ex){
    if(ex.getUserId() != null)
      log.warn("Limit reached. User ID {}. Message {}", ex.getUserId(), ex.getMessage());
    else
      log.warn("Limit reached. IP {} endpoint {}. Message {}", ex.getIpAddress(), ex.getEndpoint(), ex.getMessage());
    // return new ResponseEntity<>(new ErrorMessage(ex.getCode(), ex.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfter()))
        .header("X-RateLimit-Limit", String.valueOf(ex.getRequestLimit()))
        .header("X-RateLimit-Remaining", "0")
        .body(new ErrorMessage(ex.getCode(), ex.getMessage()));
  }
}
