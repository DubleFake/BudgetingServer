package org.dfproductions.budgetingserver.backend.templates;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
public class User {

    @Id
    @Column("ID")
    private int id;

    @Column("Name")
    private String name;
    @Column("Email")
    private String email;
    @Column("PasswordID")
    private int passwordId;

    public User(String name, String email, int passwordId){
        this.email = email;
        this.passwordId = passwordId;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }

}
