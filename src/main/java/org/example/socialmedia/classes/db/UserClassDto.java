package org.example.socialmedia.classes.db;

import java.util.List;

public class UserClassDto {
    private String username;
    private String email;
    private String news;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNews() {
        return news;
    }

    public UserClassDto(String username, String email, String news) {
        this.username = username;
        this.email = email;
        this.news = news;
    }
}
