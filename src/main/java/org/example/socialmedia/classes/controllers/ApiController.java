package org.example.socialmedia.classes.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.*;
import org.example.socialmedia.classes.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${TokenMaxAge}")
    int COOKIE_MAX_AGE; // in days

    AuthenticationManager authenticationManager;
    JwtService jwtService;
    Dao dao;
    PasswordEncoder passwordEncoder;
    @Autowired
    public ApiController(AuthenticationManager authenticationManager, JwtService jwtService, Dao dao, PasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }
    
    // Users table logic
    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletResponse response, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) throws IOException {
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Save cookie and set auth to securitycontext
            DoAuthentication(response, username, password);
            response.sendRedirect("/socialmedia");
            
            return null;
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login or password is incorrect");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(HttpServletResponse response,@RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "confirmPassword") String confirmPassword) throws IOException {
        if (!password.equals(confirmPassword)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Passwords are not same");

        // Is credentials exist check
        if(dao.isUserExist(username)) return ResponseEntity.status(HttpStatus.CONFLICT).body("User with that username is exist");
        if(dao.isEmailExist(email)) return ResponseEntity.status(HttpStatus.CONFLICT).body("User with that email is exist");

        // Saving user to db
        UserClass user = new UserClass(username, email, passwordEncoder.encode(password), "ROLE_USER");
        dao.saveUser(user);

        // Save cookie and set auth to security context
        DoAuthentication(response, username, password);
        response.sendRedirect("/socialmedia");

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/getusers")
    public List<UserClassDto> getUsers(){
        return dao.getUsers();
    }

    @GetMapping("/getmynews")
    public List<NewsDto> getMyNews(){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return dao.getUserNews(myUsername);
    }

    @GetMapping("/getusersbypage")
    public List<UserClassDto> getUsersByPage(@RequestParam(name = "page") int page){
        return dao.getUsersByPage(page);
    }

    // News table logic
    @GetMapping("/getallnews")
    public List<NewsDto> getAllNews(){
        return dao.getNews();
    }

    // Init users and news
    @PostConstruct
    public void init(){ // String username, String email, String password, String role
        UserClass user1 = new UserClass("admin", "admin@admin.com", passwordEncoder.encode("admin"), "ROLE_ADMIN");
        UserClass user2 = new UserClass("bobr","user@site.com", passwordEncoder.encode("1234"),"ROLE_USER");
        dao.saveUser(user1);
        dao.saveUser(user2);

        // String date, String title, String content, UserClass author
        News news;
        news = new News("20.3.2003", "Iraq is bombed", "Usa bombed Iraq today", user1);
        dao.saveNews(news);
        news = new News("29.3.2020", "Syria is bombed", "Syria is bombed today", user1);
        dao.saveNews(news);
        news = new News(LocalDate.now().toString(), "Gooooool", "Gooool in soccer!", user2);
        dao.saveNews(news);
    }

    // Generate and set cookie and send authentication to security context
    private void DoAuthentication(HttpServletResponse response, String username, String password) {
        // Trying to authenticate with got username n password
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Creating and sending access cookie to user
        Cookie cookie = new Cookie("Token", jwtService.generateToken(username));

        cookie.setMaxAge(COOKIE_MAX_AGE * 60 * 60 * 24);
        cookie.setPath("/socialmedia");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);

        response.addCookie(cookie);
    }


}
