package org.example.socialmedia.classes.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.*;
import org.example.socialmedia.classes.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

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

    @GetMapping("/getMyUsername")
    public String getMyUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
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
    @GetMapping("/getAllNews")
    public List<NewsDto> getAllNews(){
        return dao.getNews();
    }

    @PostMapping("/newNews")
    public ResponseEntity<String> newNews(HttpServletResponse response, @RequestParam(name = "title") String title, @RequestParam(name = "content") String content) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserClass author = dao.getUserByUsername(username);
        if(dao.isTitleNewsExist(title)) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        News news = new News(getCurrentDateTime(), title, content, author);
        dao.saveNews(news);

        response.sendRedirect("/socialmedia");

        return ResponseEntity.status(HttpStatus.OK).build();
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
        news = new News("02.11.1992 11:32", "Iraq is bombed", "Usa bombed Iraq today", user1);
        dao.saveNews(news);
        news = new News("12.11.2006 03:22", "Syria is bombed", "Syria is bombed today", user1);
        dao.saveNews(news);
        news = new News(getCurrentDateTime(), "Gooooool", "Gooool in soccer!", user2);
        dao.saveNews(news);
        news = new News(getCurrentDateTime(), "Epidemic of COVID-19", "Epidemic of Covid had been started", user2);
        dao.saveNews(news);
    }

    // Generate and set cookie and send authentication to security context

    public String getCurrentDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.now().format(formatter);
    }

}
