package com.example.twist.model.profile;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("displayName")
    private String displayName;

    @SerializedName("username")
    private String username;

    @SerializedName("bio")
    private String bio;

    @SerializedName("email")
    private String email;

    public UpdateProfileRequest(String displayName, String username, String bio, String email) {
        this.displayName = displayName;
        this.username = username;
        this.bio = bio;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}