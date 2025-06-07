package com.example.twist.model.auth;

public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String confirmPassword;
    private String token; // Untuk reset password

    // Constructor untuk register
    public AuthRequest(String username, String password, String email, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmPassword = confirmPassword;
    }

    // Constructor untuk login
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor untuk forgot password
    public AuthRequest(String email) {
        this.email = email;
    }

    // Constructor untuk reset password
    public AuthRequest(String token, String password, String confirmPassword) {
        this.token = token;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}