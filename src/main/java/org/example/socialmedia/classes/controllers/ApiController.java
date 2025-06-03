package org.example.socialmedia.classes.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.Dao;
import org.example.socialmedia.classes.db.UserClass;
import org.example.socialmedia.classes.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ApiController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    Dao dao;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity login(HttpServletResponse response, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            SecurityContextHolder.getContext().setAuthentication(auth);

            Cookie cookie = new Cookie("JWT", jwtService.generateToken(username));
            response.addCookie(cookie);
            response.sendRedirect("/");

            return null;
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password is incorrect");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect error");
        }

    }
    @PostMapping("/register")
    public ResponseEntity register(HttpServletResponse response,@RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "confirmPassword") String confirmPassword) {
        if (!password.equals(confirmPassword)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Passwords are not same");
        if (dao.isUsernameExist(username)) return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this username already exist");
        if (dao.isEmailExist(email)) return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exist");

        UserClass user = new UserClass(username, email, encoder.encode(password), "user");
        dao.saveUser(user);

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        try{
            response.sendRedirect("/");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect error");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }
    @PostMapping("/getUsers")
    public List<UserClass> getUsers(){
        return dao.getUsers();
    }

    // Init users
    @PostConstruct
    public void init(){ // String username, String email, String password, String role
        dao.saveUser(new UserClass("admin", "admin@admin.com", encoder.encode("admin"),"admin"));
        dao.saveUser(new UserClass("bob","user@site.com", encoder.encode("1234"),"user"));
    }
}
