package com.example.twist.activity.home;

import android.util.Log;

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
        if (view != null && token != null) {
            view.showLoading();
            Call<List<PostResponse>> call = apiService.getPosts("Bearer " + token);
            call.enqueue(new Callback<List<PostResponse>>() {
                @Override
                public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                    view.hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("HomePresenter", "Posts loaded: " + response.body().size());
                        view.hideWelcomeMessage();
                        view.showTwistList(response.body());
                    } else {
                        Log.e("HomePresenter", "Error: " + response.code() + ", Body: " + (response.errorBody() != null ? response.errorBody().toString() : "No detail"));
                        view.showError("Gagal memuat twist: Kode " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                    view.hideLoading();
                    Log.e("HomePresenter", "Network error: " + t.getMessage());
                    view.showError("Koneksi bermasalah: " + t.getMessage());
                }
            });
        } else {
            Log.e("HomePresenter", "Token tidak ditemukan");
            view.showError("Token tidak ditemukan, silakan login");
        }
    }

    public void likePost(int postId) {
        if (token != null) {
            view.showLoading();
            Log.d("HomePresenter", "Like request for postId: " + postId); // Debug
            Call<Void> call = apiService.likePost("Bearer " + token, postId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    view.hideLoading();
                    if (response.isSuccessful()) {
                        Log.d("HomePresenter", "Like successful for postId: " + postId);
                        loadData(); // Refresh data
                    } else {
                        Log.e("HomePresenter", "Like failed: " + response.code());
                        view.showError("Gagal menyukai twist: Kode " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    view.hideLoading();
                    Log.e("HomePresenter", "Like network error: " + t.getMessage());
                    view.showError("Koneksi bermasalah: " + t.getMessage());
                }
            });
        }
    }

    public void repostPost(int postId) {
        if (token != null) {
            view.showLoading();
            Log.d("HomePresenter", "Repost request for postId: " + postId); // Debug
            Call<Void> call = apiService.repostPost("Bearer " + token, postId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    view.hideLoading();
                    if (response.isSuccessful()) {
                        Log.d("HomePresenter", "Repost successful for postId: " + postId);
                        loadData(); // Refresh data
                    } else {
                        Log.e("HomePresenter", "Repost failed: " + response.code());
                        view.showError("Gagal merepost twist: Kode " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    view.hideLoading();
                    Log.e("HomePresenter", "Repost network error: " + t.getMessage());
                    view.showError("Koneksi bermasalah: " + t.getMessage());
                }
            });
        }
    }

    public void commentPost(int postId) {
        Log.d("HomePresenter", "Comment request for postId: " + postId); // Debug
        if (view instanceof HomeView) {
            ((HomeView) view).showCommentInput(postId);
        }
    }

    public void viewComments(int postId) {
        Log.d("HomePresenter", "View Comments request for postId: " + postId); // Debug
        if (view instanceof HomeView) {
            ((HomeView) view).showCommentList(postId);
        }
    }
}