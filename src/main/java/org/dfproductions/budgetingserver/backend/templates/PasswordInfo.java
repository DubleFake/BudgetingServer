package org.dfproductions.budgetingserver.backend.templates;

public class PasswordInfo {
    private String passwordHash;
    private String passwordSalt;

    // Getters and Setters
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
}
