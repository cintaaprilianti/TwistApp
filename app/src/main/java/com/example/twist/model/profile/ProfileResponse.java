package com.example.twist.model.profile;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("bio")
    private String bio;

    @SerializedName("email")
    private String email;

    @SerializedName("followerCount")
    private int followerCount;

    @SerializedName("followingCount")
    private int followingCount;

    @SerializedName("postsCount")
    private int postsCount;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("isVerified")
    private boolean isVerified;

    @SerializedName("isActive")
    private boolean isActive;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username != null ? username : "";
    }

    public String getDisplayName() {
        return displayName != null ? displayName : getUsername(); // Fallback ke username jika displayName null
    }

    public String getBio() {
        return bio != null ? bio : "";
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt : "";
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public boolean getIsActive() {
        return isActive;
    }
}