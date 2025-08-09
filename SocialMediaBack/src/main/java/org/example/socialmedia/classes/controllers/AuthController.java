package org.example.socialmedia.classes.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socialmedia.classes.db.Dao;
import org.example.socialmedia.classes.db.UserClass;
import org.example.socialmedia.classes.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Value("${token-max-age}")
    long COOKIE_MAX_AGE; // in days

    private Dao dao;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(Dao dao, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder){
        this.dao = dao;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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

            return ResponseEntity.status(302).header("Location", "/socialmedia").build();
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) throws IOException {
        // Creating empty cookie
        Cookie emptyCookie = new Cookie("JWT",null);
        // Sending empty cookie
        response.addCookie(emptyCookie);
        emptyCookie.setHttpOnly(true);
        emptyCookie.setSecure(false);
        emptyCookie.setPath("/");
        emptyCookie.setMaxAge(0);
        // Sending empty security authentication
        SecurityContextHolder.clearContext();
        // Redirecting to login page
        response.sendRedirect("/socialmedia/login");

        return ResponseEntity.ok().build();
    }

    private void DoAuthentication(HttpServletResponse response, String username, String password) {
        // Trying to authenticate with got username n password
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Creating and sending access cookie to user
        ResponseCookie cookie = ResponseCookie.from("JWT", jwtService.generateToken(username))
                .httpOnly(true)
                .secure(false)
                .path("/socialmedia")
                .maxAge(COOKIE_MAX_AGE * 60 * 60 * 24)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
