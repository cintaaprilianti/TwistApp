package com.example.twist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.profile.EditPostActivity;
import com.example.twist.activity.profile.ProfileActivity;
import com.example.twist.adapter.ProfileTwistAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostResponse;
import com.example.twist.model.profile.ProfilePostsResponse;
import com.example.twist.util.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePostsFragment extends Fragment {

    private static final String TAG = "ProfilePostsFragment";
    private RecyclerView recyclerView;
    private ProfileTwistAdapter adapter;
    private ApiService apiService;
    private String username;
    private SessionManager sessionManager;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_twist, container, false);

        recyclerView = view.findViewById(R.id.twist_recycler_view);
        adapter = new ProfileTwistAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(requireContext());
        username = getArguments() != null ? getArguments().getString("username") : null;
        Log.d(TAG, "Username: " + username);

        setupRecyclerView();
        loadPosts();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && hasMore) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5 && firstVisibleItemPosition >= 0) {
                        loadPosts();
                    }
                }
            }
        });

        adapter.setOnTwistActionListener(new ProfileTwistAdapter.OnTwistActionListener() {
            @Override
            public void onEditTwist(int postId, String content) {
                Intent intent = new Intent(getContext(), EditPostActivity.class);
                intent.putExtra("postId", postId);
                intent.putExtra("content", content);
                startActivity(intent);
            }

            @Override
            public void onDeleteTwist(int postId) {
            }

            @Override
            public void onLikeTwist(int postId, boolean isLiked) {
            }

            @Override
            public void onCommentTwist(int postId) {
                Toast.makeText(getContext(), "Fitur komentar belum diimplementasikan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRepostTwist(int postId) {
            }
        });
    }

    private void loadPosts() {
        if (username != null && !isLoading && hasMore) {
            isLoading = true;
            String token = sessionManager.getToken();
            Log.d(TAG, "Token: " + (token != null ? token.substring(0, 10) + "..." : "null"));
            if (token != null) {
                Call<ProfilePostsResponse> call = apiService.getProfilePosts("Bearer " + token, username, "posts", 10, currentPage * 10);
                call.enqueue(new Callback<ProfilePostsResponse>() {
                    @Override
                    public void onResponse(Call<ProfilePostsResponse> call, Response<ProfilePostsResponse> response) {
                        isLoading = false;
                        Log.d(TAG, "API Response Code: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            ProfilePostsResponse postsResponse = response.body();
                            List<PostResponse> posts = postsResponse.getPosts();
                            hasMore = postsResponse.getPagination().isHasMore();
                            List<PostResponse> currentList = currentPage == 0 ? new ArrayList<>() : adapter.getTwistList();
                            currentList.addAll(posts);
                            adapter.setTwistList(currentList);
                            int currentUserId = getActivity() instanceof ProfileActivity
                                    ? ((ProfileActivity) getActivity()).getCurrentUserId() : -1;
                            adapter.setCurrentUserId(currentUserId);
                            currentPage++;
                        } else {
                            String errorMsg = "Gagal memuat postingan: " + response.code();
                            if (response.code() == 404) {
                                errorMsg = "Profil tidak ditemukan";
                            } else if (response.code() == 400) {
                                errorMsg = "Parameter tab tidak valid";
                            }
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                            try {
                                Log.e(TAG, "Gagal memuat postingan. Response: " + (response.errorBody() != null ? response.errorBody().string() : "Tidak ada error body"));
                            } catch (IOException e) {
                                Log.e(TAG, "Error membaca error body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfilePostsResponse> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error jaringan: " + t.getMessage(), t);
                    }
                });
            } else {
                isLoading = false;
                Toast.makeText(getContext(), "Token autentikasi tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        } else if (username == null) {
            Toast.makeText(getContext(), "Username tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }
}