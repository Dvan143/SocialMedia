package org.example.socialmedia.classes.db;

import jakarta.persistence.*;

import java.util.List;

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
    private boolean isEmailVerified = false;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private String birthday;

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

    public UserClass(String username, String email, String password, String role, String birthday){
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=role;
        this.birthday = birthday;
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

    public boolean emailVerified(){
        return isEmailVerified;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getBirthday() {
        return birthday;
    }

    public List<News> getNews() {
        return news;
    }

    // TODO
    public void verifyEmail(){
        isEmailVerified = true;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
