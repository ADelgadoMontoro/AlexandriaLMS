package com.alexandrialms.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Password {
    private int passwordID;
    private int userID;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public int getPasswordID() {
        return passwordID;
    }
    public void setPasswordID(int passwordID) {
        this.passwordID = passwordID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Password() {
    }
    public Password(int passwordID, int userID, String passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.passwordID = passwordID;
        this.userID = userID;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "Password [passwordID=" + passwordID + ", userID=" + userID + ", passwordHash=" + passwordHash
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
    

}
