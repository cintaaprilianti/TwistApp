package com.example.twist.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.home.HomePresenter;
import com.example.twist.activity.home.HomeView;
import com.example.twist.activity.post.CommentActivity;
import com.example.twist.activity.post.CommentListActivity;
import com.example.twist.adapter.TwistAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwistFragment extends Fragment implements HomeView {

    private RecyclerView recyclerView;
    private TwistAdapter twistAdapter;
    private ApiService apiService;
    private HomePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twist_fragment, container, false);

        recyclerView = view.findViewById(R.id.twistRecyclerView);
        twistAdapter = new TwistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(twistAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        String token = requireActivity().getSharedPreferences("TwistPrefs", MODE_PRIVATE).getString("auth_token", null);
        presenter = new HomePresenter(this, apiService, token);
        twistAdapter.setOnItemInteractionListener(new TwistAdapter.OnItemInteractionListener() {
            @Override
            public void onLikeClicked(int postId) {
                presenter.likePost(postId);
            }

            @Override
            public void onRepostClicked(int postId) {
                presenter.repostPost(postId);
            }

            @Override
            public void onCommentClicked(int postId) {
                presenter.commentPost(postId);
            }

            @Override
            public void onViewCommentsClicked(int postId) {
                presenter.viewComments(postId);
            }
        });

        if (token != null) {
            presenter.loadData();
        } else {
            Toast.makeText(getContext(), "Silakan login", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void showWelcomeMessage() {
        // Implementasi jika perlu
    }

    @Override
    public void hideWelcomeMessage() {
        // Implementasi jika perlu
    }

    @Override
    public void showTwistList(List<PostResponse> posts) {
        twistAdapter.setTwistList(posts);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        // Implementasi loading (misalnya ProgressBar)
    }

    @Override
    public void hideLoading() {
        // Implementasi hide loading
    }

    @Override
    public void showCommentInput(int postId) {
        // Intent ke aktivitas untuk input komentar, misalnya CommentActivity
        Intent intent = new Intent(getContext(), CommentActivity.class); // Buat CommentActivity
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    @Override
    public void showCommentList(int postId) {
        // Intent ke aktivitas untuk daftar komentar, misalnya CommentListActivity
        Intent intent = new Intent(getContext(), CommentListActivity.class); // Buat CommentListActivity
        intent.putExtra("postId", postId);
        startActivity(intent);
    }
}