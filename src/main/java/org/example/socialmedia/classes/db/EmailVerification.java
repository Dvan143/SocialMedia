package org.example.socialmedia.classes.db;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserClass user;
    @Column
    private String code;
    @Column
    private Date expiresAt;
    @Column
    private byte attempts;

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public EmailVerification() {
    }

    public EmailVerification(UserClass userClass) {
        this.user = userClass;
        this.attempts = 3;
    }

    public void minusAttempt() {
        this.attempts--;
    }

    public byte getAttempts(){
        return attempts;
    }
}
