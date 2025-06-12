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
import java.util.List;

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

    @GetMapping("/news")
    public String news(HttpServletResponse response) throws IOException {
        ifAuthorizedRedirect(response);
        return "news";
    }

    private boolean isAuthenticated(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
    private void ifUnauthorizedRedirect(HttpServletResponse response) throws IOException {
        if(!isAuthenticated()) response.sendRedirect("/login");
    }
    private void ifAuthorizedRedirect(HttpServletResponse response) throws IOException {
        if(isAuthenticated()) response.sendRedirect("/");
    }
}
