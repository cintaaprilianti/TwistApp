package com.example.twist.model.post;

public class QuotePostPayload {
    private String quoteContent;
    private boolean isQuotePost;

    public QuotePostPayload(String quoteContent) {
        this.quoteContent = quoteContent;
        this.isQuotePost = true;
    }

    public String getQuoteContent() {
        return quoteContent;
    }
    public boolean getIsQuotePost() {
        return isQuotePost;
    }
}