package org.example.socialmedia.classes.controllers;

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
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<String> ForbiddenException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden act");
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<String> NotFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> IoException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> AnyOtherException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}
