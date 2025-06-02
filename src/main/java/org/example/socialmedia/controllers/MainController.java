package org.example.socialmedia.controllers;

import org.example.SocialNetwork.classes.NewsDB;
import org.example.SocialNetwork.classes.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class MainController {
    UserDB userDb;
    NewsDB newsDb;

    @Autowired
    public MainController(UserDB userDb, NewsDB newsDb) {
        this.userDb = userDb;
        this.newsDb = newsDb;
    }

    @GetMapping("/hi")
    public String index() {
        return "index";
    }
    @GetMapping("/profile/{id}")
    public String hello(@PathVariable("id") int id, Model model) {
        model.addAttribute("userName", userDb.getUserById(id));
        model.addAttribute("email", userDb.getEmailById(id));
        return "profile";
    }
    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("allNews", newsDb.getNewsList()[0]);
        model.addAttribute("lastNews", newsDb.getNewsList()[1]);
        return "news";
    }
}
