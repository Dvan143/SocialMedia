package org.example.socialmedia.classes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;

        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            filterChain.doFilter(request,response);
            return;
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("JWT")) token = cookie.getValue();
        }


        if(token==null || token.equals("")){
            filterChain.doFilter(request,response);
            return;
        }


        username = jwtService.extractUsername(token);
        if(!jwtService.verifyToken(token,username)){
            filterChain.doFilter(request,response);
            return;
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails user = null;
            try{
                user = customUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                Cookie emptyCookie = new Cookie("JWT",null);
                response.addCookie(emptyCookie);
                SecurityContextHolder.clearContext();
                response.sendRedirect("/socialmedia");
                return;
            }
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request,response);
    }
}
