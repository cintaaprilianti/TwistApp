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
    private Integer parentPostId; // Nullable, karena tidak semua post adalah reply

    // Placeholder untuk isLiked (harus diisi dari backend atau logika lokal)
    private boolean isLiked;

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

    public int getCommentCount() {
        return commentCount;
    }

    public int getRepostCount() {
        return repostCount;
    }

    public boolean getIsQuotePost() {
        return isQuotePost;
    }

    public Integer getParentPostId() {
        return parentPostId;
    }

    public boolean getIsLiked() {
        return isLiked; // Bisa diatur dari response backend
    }

    // Setter untuk isLiked (opsional, untuk pengujian atau logika lokal)
    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}