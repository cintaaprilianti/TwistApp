package com.example.twist.fragment;

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
import com.example.twist.activity.profile.ProfileActivity;
import com.example.twist.adapter.ProfileTwistAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.profile.ProfileResponse;
import com.example.twist.model.post.PostResponse;
import com.example.twist.util.SessionManager;

import java.io.IOException;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_twist, container, false);

        recyclerView = view.findViewById(R.id.twist_recycler_view);
        adapter = new ProfileTwistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(requireContext());
        username = getArguments() != null ? getArguments().getString("username") : null;
        Log.d(TAG, "Username: " + username);

        loadPosts();
        return view;
    }

    private void loadPosts() {
        if (username != null) {
            String token = sessionManager.getToken();
            Log.d(TAG, "Token: " + (token != null ? token.substring(0, 10) + "..." : "null"));
            if (token != null) {
                Call<ProfileResponse> call = apiService.getProfilePosts("Bearer " + token, username, "posts", 10, 0);
                call.enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        Log.d(TAG, "API Response Code: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            List<PostResponse> posts = response.body().getData();
                            adapter.setTwistList(posts);
                            int currentUserId = getActivity() instanceof ProfileActivity
                                    ? ((ProfileActivity) getActivity()).getCurrentUserId() : -1;
                            adapter.setCurrentUserId(currentUserId);
                            adapter.setOnTwistActionListener(new ProfileTwistAdapter.OnTwistActionListener() {
                                @Override
                                public void onEditTwist(int postId) {
                                    Toast.makeText(getContext(), "Edit Twist: " + postId, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDeleteTwist(int postId) {
                                    Toast.makeText(getContext(), "Delete Twist: " + postId, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Failed to load posts: " + response.code(), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e(TAG, "Failed to load posts. Response: " + (response.errorBody() != null ? response.errorBody().string() : "No error body"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Network error: " + t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getContext(), "No authentication token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Username not provided", Toast.LENGTH_SHORT).show();
        }
    }
}