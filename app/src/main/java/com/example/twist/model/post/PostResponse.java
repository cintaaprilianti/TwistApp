package com.example.twist.model.post;

import com.example.twist.model.profile.ProfileResponse;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("content")
    private String content;

    @SerializedName("user")
    private ProfileResponse user;

    @SerializedName("likeCount")
    private int likeCount;

    @SerializedName("commentCount")
    private int commentCount;

    @SerializedName("repostCount")
    private int repostCount;

    @SerializedName("isQuotePost")
    private boolean isQuotePost;

    @SerializedName("parentPostId")
    private Integer parentPostId;

    @SerializedName("isLiked")
    private boolean isLiked;

    @SerializedName("isReposted")
    private boolean isReposted;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("isEdited")
    private boolean isEdited;

    @SerializedName("isPinned")
    private boolean isPinned;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content != null ? content : "";
    }

    public ProfileResponse getUser() {
        return user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getRepostCount() {
        return repostCount;
    }

    public void setRepostCount(int repostCount) {
        this.repostCount = repostCount;
    }

    public boolean getIsQuotePost() {
        return isQuotePost;
    }

    public Integer getParentPostId() {
        return parentPostId;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean getIsReposted() {
        return isReposted;
    }

    public void setIsReposted(boolean isReposted) {
        this.isReposted = isReposted;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt : "";
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt : "";
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }
}