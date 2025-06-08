package com.example.twist.activity.home;

import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private HomeView view;
    private ApiService apiService;
    private String token;

    public HomePresenter(HomeView view, ApiService apiService, String token) {
        this.view = view;
        this.apiService = apiService;
        this.token = token;
    }

    public void loadData() {
        if (token == null) {
            view.showWelcomeMessage();
            return;
        }

        view.showLoading();
        Call<List<PostResponse>> call = apiService.getPosts("Bearer " + token);
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    List<PostResponse> posts = response.body();
                    if (posts.isEmpty()) {
                        view.showWelcomeMessage();
                    } else {
                        view.hideWelcomeMessage();
                        view.showTwistList(posts);
                    }
                } else {
                    view.showError("Gagal memuat twist: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                view.hideLoading();
                view.showError("Koneksi bermasalah: " + t.getMessage());
            }
        });
    }
}