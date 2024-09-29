package org.dfproductions.budgetingserver.backend.templates;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "passwords")
public class Password {

    @Id
    @Column("ID")
    private int id;

    @Column("PasswordHash")
    private String passwordHash;
    @Column("PasswordSalt")
    private String passwordSalt;
    @Column("UserID")
    private int userId;

    public Password(String passwordHash, String passwordSalt, int userId) {
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.userId = userId;
    }

    public Password(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
