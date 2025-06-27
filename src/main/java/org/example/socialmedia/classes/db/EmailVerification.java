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
    private String codeHash;
    @Column
    private boolean isVerified;
    @Column
    private Date createdAt;
    @Column
    private Date expiresAt;

    public String getCodeHash() {
        return codeHash;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public UserClass getUser() {
        return user;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
//    public EmailVerification(UserClass user, String codeHash, Date createdAt, Date expiresAt) {
//        this.user = user;
//        this.codeHash = codeHash;
//        this.createdAt = createdAt;
//        this.expiresAt = expiresAt;
//        this.isVerified = false;
//    }

    public EmailVerification(UserClass userClass) {
        this.user = userClass;
        this.isVerified=false;
        userClass.setEmailVerification(this);
    }

    public EmailVerification() {

    }
}
