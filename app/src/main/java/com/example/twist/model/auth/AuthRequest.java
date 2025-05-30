package com.example.twist.model.auth;

public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String confirmPassword;

    public AuthRequest(String username, String password, String email, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmPassword = confirmPassword;
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}