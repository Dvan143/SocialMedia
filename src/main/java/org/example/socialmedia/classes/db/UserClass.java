package org.example.socialmedia.classes.db;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class UserClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String role;

    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<News> news;

//    @ManyToMany(mappedBy = "friends")
//    @JoinTable(name = "user_friends",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "friend_id"))
//    private Set<UserClass> friends;

    public UserClass(){
    }

    public UserClass(String username, String email, String password, String role){
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=role;
    }

    public UserClass(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<News> getNews() {
        return news;
    }
}
