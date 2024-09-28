package org.dfproductions.budgetingserver.backend.templates;

import jakarta.persistence.*;

@Entity
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String hash;
    private String salt;
    private int userId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Password(String hash, String salt, int userId) {
        this.hash = hash;
        this.salt = salt;
        this.userId = userId;
    }

    public Password(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
