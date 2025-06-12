package org.example.socialmedia.classes.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.*;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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
    // Users
    @PostMapping("/login")
    public ResponseEntity login(HttpServletResponse response, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) throws IOException {
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            SecurityContextHolder.getContext().setAuthentication(auth);

            Cookie cookie = new Cookie("Token", jwtService.generateToken(username));
            cookie.setMaxAge(60*60*24*3);
            cookie.setPath("/socialmedia");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            
            response.addCookie(cookie);
            response.sendRedirect("/socialmedia");

            return null;
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password is incorrect");
        }

    }

    @PostMapping("/register")
    public ResponseEntity register(HttpServletResponse response,@RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "confirmPassword") String confirmPassword) throws IOException {
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
        cookie.setMaxAge(60*60*24*3);
        cookie.setPath("/socialmedia");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);

        response.sendRedirect("/socialmedia");

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/getUsers")
    public List<UserClassDto> getUsers(@RequestParam(name = "page") int page){
        return dao.getUsersByPage(page);
    }

    // News
    @GetMapping("/getAllNews")
    public List<NewsDto> getAllNews(){
        return dao.getAllNews();
    }

    // Init users
    @PostConstruct
    public void init(){ // String username, String email, String password, String role
        UserClass admin = new UserClass("admin", "admin@admin.com", encoder.encode("admin"), "admin");
        UserClass user = new UserClass("bob","user@site.com", encoder.encode("1234"),"user");
        dao.saveUser(admin);
        dao.saveUser(user);

        // String date, String title, String content, UserClass author
        News news;
        news = new News("20.3.2003", "Iraq is bombed", "Usa bombed Iraq today", admin);
        dao.saveNews(news);
        news = new News(LocalDate.now().toString(), "Gooooool", "Gooool in soccer!", user);
        dao.saveNews(news);
    }
}
