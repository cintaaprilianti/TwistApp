package com.example.twist.model.auth;

public class AuthResponse {
    private User user;
    private String token;
    private String message;
    private String resetToken;
    private String resetLink;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getResetToken() {
        return resetToken;
    }
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public String getResetLink() {
        return resetLink;
    }
    public void setResetLink(String resetLink) {
        this.resetLink = resetLink;
    }
}