package com.example.twist.model.post;

public class CommentPayload {
    private String content;

    public CommentPayload(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}