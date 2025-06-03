package org.example.socialmedia.classes.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    Dao dao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserClass user = null;
            user = dao.getUserByUsername(username);
            return User.builder().username(user.getUsername()).password(user.getPassword()).authorities(user.getRole()).build();
    }
}
