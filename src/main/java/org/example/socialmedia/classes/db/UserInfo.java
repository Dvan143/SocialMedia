package org.example.socialmedia.classes.db;

import jakarta.persistence.*;

@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String codeForResetPassword;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserClass user;

    public UserInfo() {
    }

    public UserInfo(UserClass user) {
        this.codeForResetPassword = codeForResetPassword;
        setUser(user);
    }

    public void setUser(UserClass user) {
        this.user = user;
    }

    public String getCodeForResetPassword() {
        return codeForResetPassword;
    }

    public void setCodeForResetPassword(String code) {
        this.codeForResetPassword = code;
    }



}
