package com.example.twist.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.post.AddTwistActivity;
import com.example.twist.activity.post.CommentActivity;
import com.example.twist.activity.post.CommentListActivity;
import com.example.twist.activity.profile.ProfileActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.adapter.TwistAdapter;
import com.example.twist.model.post.PostResponse;
import com.example.twist.util.SessionManager;

import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private RecyclerView twistRecyclerView;
    private TwistAdapter twistAdapter;
    private TextView welcomeText;
    private EditText searchBar;
    private BottomNavigationView bottomNav;
    private HomePresenter presenter;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        twistRecyclerView = findViewById(R.id.twist_recycler_view);
        welcomeText = findViewById(R.id.welcomeText);
        searchBar = findViewById(R.id.search_bar);
        bottomNav = findViewById(R.id.bottomNav);

        twistAdapter = new TwistAdapter();
        twistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        twistRecyclerView.setAdapter(twistAdapter);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String token = sessionManager.getToken();
        Log.d("HomeActivity", "Token: " + (token != null ? token.substring(0, 10) + "..." : "null"));
        presenter = new HomePresenter(this, apiService, token);

        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        twistAdapter.setOnItemInteractionListener(new TwistAdapter.OnItemInteractionListener() {
            @Override
            public void onLikeClicked(int postId) {
                Log.d("HomeActivity", "Like clicked for postId: " + postId);
                presenter.likePost(postId);
            }

            @Override
            public void onRepostClicked(int postId) {
                Log.d("HomeActivity", "Repost clicked for postId: " + postId);
                presenter.repostPost(postId);
            }

            @Override
            public void onCommentClicked(int postId) {
                Log.d("HomeActivity", "Comment clicked for postId: " + postId);
                presenter.commentPost(postId);
            }

            @Override
            public void onViewCommentsClicked(int postId) {
                Log.d("HomeActivity", "View Comments clicked for postId: " + postId);
                presenter.viewComments(postId);
            }
        });

        if (token != null) {
            presenter.loadData();
        } else {
            Toast.makeText(this, "Silakan login untuk melihat twist", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data saat kembali ke HomeActivity
        String token = sessionManager.getToken();
        if (token != null) {
            presenter = new HomePresenter(this, ApiClient.getClient().create(ApiService.class), token);
            presenter.loadData();
        }
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            return true;
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            String token = sessionManager.getToken();
            if (token != null) {
                String username = sessionManager.getUsername();
                int userId = sessionManager.getUserId();
                if (username != null && !username.isEmpty()) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Username not found, please login again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Silakan login untuk melihat profile", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public void showWelcomeMessage() {
        welcomeText.setVisibility(View.VISIBLE);
        twistRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideWelcomeMessage() {
        welcomeText.setVisibility(View.GONE);
        twistRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTwistList(List<PostResponse> posts) {
        twistAdapter.setTwistList(posts);
        twistAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showCommentInput(int postId) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    @Override
    public void showCommentList(int postId) {
        Intent intent = new Intent(this, CommentListActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }
}