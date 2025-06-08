package com.example.twist.activity.home;

import com.example.twist.model.post.PostResponse;
import java.util.List;

public interface HomeView {
    void showWelcomeMessage();
    void hideWelcomeMessage();
    void showTwistList(List<PostResponse> posts);
    void showError(String message);
    void showLoading();
    void hideLoading();
}
