package org.example.socialmedia.classes;

import org.springframework.stereotype.Component;

@Component
public class DAO {
    public void newUser(String username, String email, String password) {
        User user = new User();
    }
}
