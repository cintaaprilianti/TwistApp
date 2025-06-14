package com.example.twist.model.search;

public class User {
    private String id;
    private String username;
    private String followersCount;
    private int profileImageResource;

    public User() {}

    public User(String id, String username, String followersCount, int profileImageResource) {
        this.id = id;
        this.username = username;
        this.followersCount = followersCount;
        this.profileImageResource = profileImageResource;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public void setProfileImageResource(int profileImageResource) {
        this.profileImageResource = profileImageResource;
    }
}