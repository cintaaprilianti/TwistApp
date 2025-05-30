package com.example.twist.model.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("user")
    private User user;

    @SerializedName("token")
    private String token;

    public User getUser() {
        return user;
    }
    public String getToken() {
        return token;
    }

    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("createdAt")
        private String createdAt;

        public int getId() {
            return id;
        }
        public String getUsername() {
            return username;
        }
        public String getEmail() {
            return email;
        }
        public String getCreatedAt() {
            return createdAt;
        }
    }
}