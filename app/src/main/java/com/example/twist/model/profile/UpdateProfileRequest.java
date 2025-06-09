package com.example.twist.model.profile;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("displayName")
    private String displayName;

    @SerializedName("bio")
    private String bio;

    @SerializedName("email")
    private String email;

    public UpdateProfileRequest(String displayName, String bio, String email) {
        this.displayName = displayName;
        this.bio = bio;
        this.email = email;
    }
}