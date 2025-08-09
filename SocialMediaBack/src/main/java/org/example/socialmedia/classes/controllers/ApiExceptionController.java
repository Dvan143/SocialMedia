package org.example.socialmedia.classes.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RestControllerAdvice
public class ApiExceptionController {
    Logger log = LoggerFactory.getLogger(ApiExceptionController.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authException(AuthenticationException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameException(UsernameNotFoundException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<String> ForbiddenException(HttpClientErrorException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden act");
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<String> NotFoundException(HttpClientErrorException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> IoException(IOException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> AnyOtherException(Exception ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}
