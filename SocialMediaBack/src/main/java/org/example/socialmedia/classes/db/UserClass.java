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
    private Boolean isEmailVerified;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private String birthday;
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private EmailVerification emailVerification;
    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<News> news;

    public UserClass(){
    }

    public UserClass(String username, String email, String password, String role){
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=role;
        this.birthday="None";
        this.isEmailVerified=false;

        EmailVerification ev = new EmailVerification(this);
        this.emailVerification=ev;
    }

    public UserClass(String username, String email, String password, String role, String birthday){
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=role;
        this.birthday = birthday;
        this.isEmailVerified=false;

        EmailVerification ev = new EmailVerification(this);
        this.emailVerification=ev;
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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailVerified(){
        this.isEmailVerified = true;
    }

    public EmailVerification getEmailVerification() {
        return emailVerification;
    }

    public void minusAttemptForResetCode(){
        emailVerification.minusAttempt();
    }

    public byte getAttempts(){
        return emailVerification.getAttempts();
    }
}
