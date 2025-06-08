package com.example.twist.model.post;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class PostResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("content")
    private String content;

    @SerializedName("likeCount")
    private int likeCount;

    @SerializedName("commentCount")
    private int commentCount;

    @SerializedName("repostCount")
    private int repostCount;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("isEdited")
    private boolean isEdited;

    @SerializedName("isPinned")
    private boolean isPinned;

    @SerializedName("user")
    private User user;

    public int getId() { return id; }
    public String getContent() {
        return content;
    }
    public int getLikeCount() {
        return likeCount;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public int getRepostCount() {
        return repostCount;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public boolean isEdited() {
        return isEdited;
    }
    public boolean isPinned() {
        return isPinned;
    }
    public User getUser() {
        return user;
    }

    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("username")
        private String username;

        public int getId() { return id; }
        public String getUsername() {
            return username;
        }
    }
}