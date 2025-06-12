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

public class RepliesFragment extends Fragment implements HomeView {

    private RecyclerView recyclerView;
    private TwistAdapter twistAdapter;
    private ApiService apiService;
    private HomePresenter presenter;
    private String userId;

    public RepliesFragment() {
        // Required empty public constructor
    }

    public static RepliesFragment newInstance(String userId) {
        RepliesFragment fragment = new RepliesFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twist_fragment, container, false);

        initViews(view);
        setupRecyclerView();
        setupApiService();
        loadUserReplies();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.twistRecyclerView);
    }

    private void setupRecyclerView() {
        twistAdapter = new TwistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(twistAdapter);

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
    }

    private void setupApiService() {
        apiService = ApiClient.getClient().create(ApiService.class);
        String token = requireActivity().getSharedPreferences("TwistPrefs", MODE_PRIVATE).getString("auth_token", null);
        presenter = new HomePresenter(this, apiService, token);
    }

    private void loadUserReplies() {
        String token = requireActivity().getSharedPreferences("TwistPrefs", MODE_PRIVATE).getString("auth_token", null);

        if (token != null && userId != null) {
            // TODO: Implement API call to get user replies
            // For now, show a placeholder message
            Toast.makeText(getContext(), "Loading replies from user: " + userId, Toast.LENGTH_SHORT).show();

            // Simulate API call
            loadRepliesFromApi(token, userId);
        } else {
            Toast.makeText(getContext(), "Unable to load replies", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRepliesFromApi(String token, String userId) {
        // TODO: Replace with actual API endpoint for user replies
        // Example: apiService.getUserReplies(token, userId).enqueue(callback);

        showLoading();

        // Simulate API call with delay
        new android.os.Handler().postDelayed(() -> {
            hideLoading();
            // For now, show empty state or sample data
            showError("Replies feature coming soon");
        }, 1000);
    }

    @Override
    public void showWelcomeMessage() {
        // Not needed for replies fragment
    }

    @Override
    public void hideWelcomeMessage() {
        // Not needed for replies fragment
    }

    @Override
    public void showTwistList(List<PostResponse> posts) {
        if (posts != null && !posts.isEmpty()) {
            twistAdapter.setTwistList(posts);
        } else {
            showError("No replies found");
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        // TODO: Show loading indicator
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        // TODO: Hide loading indicator
    }

    @Override
    public void showCommentInput(int postId) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    @Override
    public void showCommentList(int postId) {
        Intent intent = new Intent(getContext(), CommentListActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }
}