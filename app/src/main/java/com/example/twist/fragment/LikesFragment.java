package com.example.twist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.post.CommentActivity;
import com.example.twist.activity.post.CommentListActivity;
import com.example.twist.adapter.TwistAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.profile.ProfilePostsResponse;
import com.example.twist.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikesFragment extends Fragment {

    private static final String TAG = "LikesFragment";
    private RecyclerView recyclerView;
    private TwistAdapter twistAdapter;
    private ApiService apiService;
    private SessionManager sessionManager;
    private String username;

    public LikesFragment() {
    }

    public static LikesFragment newInstance(String userId, String username) {
        LikesFragment fragment = new LikesFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twist_fragment, container, false);

        recyclerView = view.findViewById(R.id.twistRecyclerView);
        twistAdapter = new TwistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(twistAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(getContext());

        if (getArguments() != null) {
            username = getArguments().getString("username");
            Log.d(TAG, "Received username: " + username);
        }

        twistAdapter.setOnItemInteractionListener(new TwistAdapter.OnItemInteractionListener() {
            @Override
            public void onLikeClicked(int postId) {
                likePost(postId);
            }

            @Override
            public void onRepostClicked(int postId) {
                repostPost(postId);
            }

            @Override
            public void onCommentClicked(int postId) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }

            @Override
            public void onViewCommentsClicked(int postId) {
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });

        if (username != null) {
            loadLikedPosts();
        } else {
            Toast.makeText(getContext(), "Username not available", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "username is null");
        }

        return view;
    }

    private void loadLikedPosts() {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(getContext(), "Please login to view liked posts", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Token is null");
            return;
        }

        Log.d(TAG, "Loading liked posts for username: " + username);
        Call<ProfilePostsResponse> call = apiService.getProfilePosts(
                "Bearer " + token,
                username,
                "likes",
                10,
                0
        );
        call.enqueue(new Callback<ProfilePostsResponse>() {
            @Override
            public void onResponse(Call<ProfilePostsResponse> call, Response<ProfilePostsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfilePostsResponse profilePostsResponse = response.body();
                    if (profilePostsResponse.getPosts() != null) {
                        Log.d(TAG, "Liked posts loaded: " + profilePostsResponse.getPosts().size());
                        twistAdapter.setTwistList(profilePostsResponse.getPosts());
                        twistAdapter.notifyDataSetChanged();
                        if (profilePostsResponse.getPosts().isEmpty()) {
                            Toast.makeText(getContext(), "No liked posts available for this user", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "No liked posts available", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Posts list is null");
                    }
                } else {
                    String errorMessage = "Failed to load liked posts: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage += "\nDetail: " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ProfilePostsResponse> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void likePost(int postId) {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(getContext(), "Please login to like posts", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.likePost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Like successful for postId: " + postId);
                    loadLikedPosts(); // Refresh data
                } else {
                    Log.e(TAG, "Like failed: " + response.code());
                    Toast.makeText(getContext(), "Failed to like post: Code " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Like network error: " + t.getMessage());
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void repostPost(int postId) {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(getContext(), "Please login to repost", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.repostPost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Repost successful for postId: " + postId);
                    loadLikedPosts(); // Refresh data
                } else {
                    Log.e(TAG, "Repost failed: " + response.code());
                    Toast.makeText(getContext(), "Failed to repost: Code " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Repost network error: " + t.getMessage());
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}