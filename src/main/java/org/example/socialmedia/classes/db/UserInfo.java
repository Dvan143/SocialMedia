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

    public UserInfo(UserClass userClass) {
        this.user = userClass;
    }

    public String getCodeForResetPassword() {
        return codeForResetPassword;
    }

    public void setCodeForResetPassword(String codeForResetPassword) {
        this.codeForResetPassword = codeForResetPassword;
    }
}
