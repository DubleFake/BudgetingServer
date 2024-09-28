package org.dfproductions.budgetingserver.backend.templates;

public class Password {

    private String hash;
    private String salt;
    private int userId;

    public Password(String hash, String salt, int userId) {
        this.hash = hash;
        this.salt = salt;
        this.userId = userId;
    }

    public Password(){}

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
