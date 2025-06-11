package org.example.socialmedia.classes.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            response.sendRedirect("/main");
        } else {
            response.sendRedirect("/login");
        }
        return null;
    }

    @GetMapping("/main")
    public String main(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",username);
        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

}
