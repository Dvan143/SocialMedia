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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
    public String getMyUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/getUsersByPage")
    public List<UserClassDto> getUsersByPage(@RequestParam(name = "page") int page){
        return dao.getUsersByPage(page);
    }

    // News table logic

    @PostMapping("/newNews")
    public ResponseEntity<String> newNews(HttpServletResponse response, @RequestParam(name = "title") String title, @RequestParam(name = "content") String content) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserClass author;
        try{
            author = dao.getUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(dao.isTitleNewsExist(title)) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        News news = new News(getCurrentDateTime(), title, content, author);
        dao.saveNews(news);

        response.sendRedirect("/socialmedia");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getAllNews")
    public List<NewsDto> getAllNews(){
        return dao.getNews();
    }

    @GetMapping("/getLastNews")
    public List<NewsDto> getLastNews(){
        return dao.getNewsByPage(1);
    }

    @GetMapping("/getMyNews")
    public List<NewsDto> getMyNews(){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return dao.getUserNews(myUsername);
    }

    @GetMapping("/getMyLastNews")
    public List<NewsDto> getMyLastNews(){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return dao.getUserNewsByPage(myUsername,1);
    }

    @GetMapping("/getNewsPages")
    public Long getNewsCount(){
        return dao.getNewsPages();
    }

    @GetMapping("/getNewsByPage")
    public List<NewsDto> getNewsByPage(int page){
        return dao.getNewsByPage(page);
    }

    @GetMapping("/getMyData")
    public Map<String, String> getMyData(){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return Map.of(
                "birthday", dao.getBirthday(myUsername),
                "email", dao.getEmail(myUsername),
                "isEmailVerified", String.valueOf(dao.isEmailVerified(myUsername))
                );
    }

    @GetMapping("/setMyBirthday")
    public void setMyBirthday(@RequestParam(name = "birthday") String birthday){
        String myUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        dao.setBirthday(myUsername,birthday);
    }

    @PostMapping("/changeMyPassword")
    public ResponseEntity<String> changeMyPassword(HttpServletResponse response, @RequestParam(name = "oldPassword") String oldPassword, @RequestParam(name = "newPassword") String newPassword) throws IOException {
        newPassword = passwordEncoder.encode(newPassword);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String userPassword = dao.getPassword(username);
        if (!passwordEncoder.matches(oldPassword, userPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            dao.changePassword(username, newPassword);
            response.sendRedirect("/socialmedia");
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping("/isEmailVerified")
    public String isEmailVerified(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.valueOf(dao.isEmailVerified(username));
    }

    @GetMapping("/verifyMyEmail")
    public void verifyMyEmail(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        dao.sendEmailVerificationCode(username);
    }

    @GetMapping("/checkEmailCode")
    public String checkEmailCode(@RequestParam(name = "code") String code){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.valueOf(dao.isEmailVerifyCodeCorrect(username,code));
    }

    // Init users and news
    @PostConstruct
    public void init(){ // String username, String email, String password, String role, String birthday
        UserClass user1 = new UserClass("admin", "admin@admin.com", passwordEncoder.encode("admin"), "ROLE_ADMIN","1999-12-12");
        UserClass user2 = new UserClass("bobr","user@site.com", passwordEncoder.encode("1234"),"ROLE_USER");
        dao.saveUser(user1);
        dao.saveUser(user2);

        // String date, String title, String content, UserClass author
        News news;
        news = new News(getCurrentDateTime(),"Weather in London", "Weather in London is around 20 degrees celsius",user1);
        dao.saveNews(news);
        news = new News(getCurrentDateTime(), "Gooooooool", "Gooooool in a soccer", user2);
        dao.saveNews(news);
        news = new News("12.11.2006 03:22", "Syria is bombed", "Syria is bombed today", user1);
        dao.saveNews(news);

    }

    // Generate and set cookie and send authentication to security context

    public String getCurrentDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.now().format(formatter);
    }

}
