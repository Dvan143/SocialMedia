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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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

            Cookie cookie = new Cookie("Token", jwtService.generateToken(username));
            response.addCookie(cookie);
            response.sendRedirect("/main");

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

        // Solving n+1 problem
        String usernameAndEmailIsExist = dao.isUsernameOrEmailExist(username, email);
        switch (usernameAndEmailIsExist) {
            case "Username&Email" -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username And Email Are Exist");
            }
            case "Username" -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Is Exist");
            }
            case "Email" -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Is Exist");
            }
        }

        UserClass user = new UserClass(username, email, encoder.encode(password), "user");
        dao.saveUser(user);

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        Cookie cookie = new Cookie("Token", jwtService.generateToken(username));
        response.addCookie(cookie);

        try{
            response.sendRedirect("/main");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redirect error");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/getUsers")
    public List<UserClass> getUsers(@RequestParam(name = "page") int page){
        return dao.getUsersByPage(page);
    }

    // Init users
    @PostConstruct
    public void init(){ // String username, String email, String password, String role
        dao.saveUser(new UserClass("admin", "admin@admin.com", encoder.encode("admin"),"admin"));
        dao.saveUser(new UserClass("bob","user@site.com", encoder.encode("1234"),"user"));
    }
}
