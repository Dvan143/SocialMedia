package org.example.socialmedia.classes.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public abstract class ParentController {

    protected boolean isAuthenticated(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    protected void ifUnauthorizedRedirect(HttpServletResponse response) throws IOException {
        if(!isAuthenticated()) response.sendRedirect("/socialmedia/login");
    }

    protected void ifAuthorizedRedirect(HttpServletResponse response) throws IOException {
        if(isAuthenticated()) response.sendRedirect("/socialmedia");
    }

    protected ResponseEntity sendUnauthorized() {
        return ResponseEntity.status(401).build();
    }
}
