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
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<News> news;
    // TODO friends column

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
        UserInfo uf = new UserInfo(this);
        this.userInfo = uf;
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
        UserInfo uf = new UserInfo(this);
        this.userInfo = uf;
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

    public String getBirthday() {
        return birthday;
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

    public void setEmailVerification(EmailVerification emailVerification) {
        this.emailVerification = emailVerification;
    }

    public void setNewVerifyCode(String code){
        this.userInfo.setCodeForResetPassword(code);
    }

    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public void minusAttemptForResetCode(){
        emailVerification.minusAttempt();
    }

    public byte getAttempts(){
        return emailVerification.getAttempts();
    }
}
