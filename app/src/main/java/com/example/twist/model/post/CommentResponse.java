package com.example.twist.model.post;

import com.example.twist.model.profile.ProfileResponse;
import com.google.gson.annotations.SerializedName;

public class CommentResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("content")
    private String content;

    @SerializedName("user")
    private ProfileResponse user;

    @SerializedName("createdAt")
    private String createdAt; // atau Date jika backend mengembalikan format tanggal

    public int getId() {
        return id;
    }

    public String getContent() {
        return content != null ? content : "";
    }

    public ProfileResponse getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt : "";
    }
}