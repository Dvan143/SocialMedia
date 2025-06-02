package org.example.socialmedia.controllers;

import org.example.SocialNetwork.classes.NewsDB;
import org.example.SocialNetwork.classes.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DataController {
    private final UserDB userDb;
    private final NewsDB newsDb;
    @Autowired
    public DataController(UserDB userDb, NewsDB newsDb) {
        this.userDb = userDb;
        this.newsDb = newsDb;
    }
    @PostMapping("/registerUser")
    public String register(@RequestParam("email") String email, @RequestParam("userName") String userName, @RequestParam("password") String password, Model model) {
        if(userDb.userExists(userName)){
            model.addAttribute("info","User already exists");
        } else if (userDb.emailExists(email)) {
            model.addAttribute("info","Email already exists");
        } else {
            model.addAttribute("info","User already exists");
            userDb.newUser(email, userName, password);
            model.addAttribute("info", "User registered successfully");
        }
        return "register";
    }
    @PostMapping("/auth")
    public String userPanel(@ModelAttribute("userName") String userName, @RequestParam("password") String password, Model model) {
        if(!userDb.passwordcheck(userName,password)){
            model.addAttribute("error","Invalid password");
        }else{
            return "redirect:/profile/" + userDb.getIdByName(userName);
        }
        return "index";
    }
    @PatchMapping("/resetMyPassword")
    public String resetingPassword(@RequestParam("email") String email, Model model){
        if(userDb.emailExists(email)){
            userDb.resetPasswordByEmail(email);
            model.addAttribute("info","Message has been sent");
        } else {
            model.addAttribute("info","Email does not exist");
        }
        return "resetPassword";
    }
    @PostMapping("/addNews")
    public String addNews(@RequestParam("title") String title, @RequestParam("content") String content,@RequestParam("author") String author, Model model){
        newsDb.addNews(title,content,author);
        model.addAttribute("allNews", newsDb.getNewsList()[0]);
        model.addAttribute("lastNews", newsDb.getNewsList()[1]);
        return "redirect:/news";
    }
}
