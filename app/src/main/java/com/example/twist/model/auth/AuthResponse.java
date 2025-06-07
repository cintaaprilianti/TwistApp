package com.example.twist.model.auth;

public class AuthResponse {
    private User user;
    private String token;
    private String message;
    private String resetToken; // Untuk forgot password
    private String resetLink; // Untuk forgot password

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

    public static class User {
        private int id;
        private String username;
        private String email;
        private String createdAt;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getCreatedAt() {
            return createdAt;
        }
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}