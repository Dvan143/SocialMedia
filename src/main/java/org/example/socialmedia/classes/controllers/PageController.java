package org.example.socialmedia.classes.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class PageController {
    @Autowired
    Dao dao;

    @GetMapping("/")
    public String index(HttpServletResponse response, Model model) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",username);
        ifUnauthorizedRedirect(response);
        return "main";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) throws IOException {
        ifAuthorizedRedirect(response);
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpServletResponse response) throws IOException {
        ifAuthorizedRedirect(response);
        return "register";
    }

    @GetMapping("/newNews")
    public String CreatingNews(HttpServletResponse response) throws IOException {
        ifUnauthorizedRedirect(response);
        return "newNews";
    }

    @GetMapping("/myProfile")
    public String myProfile(HttpServletResponse response) throws IOException {
        ifUnauthorizedRedirect(response);
        return "profile";
    }

    @GetMapping("/changePassword")
    public String changePassword(HttpServletResponse response) throws IOException {
        ifUnauthorizedRedirect(response);
        return "changePassword";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "resetPassword";
    }

    @GetMapping("/error/403")
    public String Unauthorized(){
        return "error/403";
    }

    @GetMapping("/error/404")
    public String NotFound(){
        return "error/404";
    }

    private boolean isAuthenticated(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
    private void ifUnauthorizedRedirect(HttpServletResponse response) throws IOException {
        if(!isAuthenticated()) response.sendRedirect("/socialmedia/login");
    }
    private void ifAuthorizedRedirect(HttpServletResponse response) throws IOException {
        if(isAuthenticated()) response.sendRedirect("/socialmedia");
    }
}
