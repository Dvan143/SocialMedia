package org.example.socialmedia.classes.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RestControllerAdvice
public class ApiExceptionController {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authException(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity usernameException(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity IoException(){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
