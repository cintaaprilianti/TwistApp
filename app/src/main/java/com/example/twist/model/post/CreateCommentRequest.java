package com.example.twist.model.post;

public class CreateCommentRequest {
    private String content;

    public CreateCommentRequest(String content) {
        this.content = content != null ? content.trim() : "";
    }

    public String getContent() {
        return content;
    }
}