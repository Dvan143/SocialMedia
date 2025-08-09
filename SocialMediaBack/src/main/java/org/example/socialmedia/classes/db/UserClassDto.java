package org.example.socialmedia.classes.db;

public class UserClassDto {
    private String username;
    private String email;
    private String news;
    private String role;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNews() {
        return news;
    }

    public String getRole() {
        return role;
    }

    public UserClassDto(String username, String email, String news, String role) {
        this.username = username;
        this.email = email;
        this.news = news;
        this.role = role;
    }
}
