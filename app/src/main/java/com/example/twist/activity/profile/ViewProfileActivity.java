package com.example.twist.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.twist.R;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.activity.post.AddTwistActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.fragment.LikesFragment;
import com.example.twist.fragment.RepliesFragment;
import com.example.twist.fragment.RepostsFragment;
import com.example.twist.fragment.TwistFragment;
import com.example.twist.model.profile.ProfileResponse;
import com.example.twist.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    private static final String TAG = "ViewProfileActivity";

    private TextView displayNameText;
    private TextView usernameText;
    private TextView bioText;
    private TextView followerCountText;
    private Button followButton;
    private ImageButton backButton;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;

    private String userId;
    private String displayName;
    private String username;
    private String bio;
    private int followerCount;
    private boolean isFollowing;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        initViews();
        setupData();
        setupClickListeners();
        setupTabs();
        setupBottomNavigation();
    }

    private void initViews() {
        displayNameText = findViewById(R.id.displayNameText);
        usernameText = findViewById(R.id.usernameText);
        bioText = findViewById(R.id.bioText);
        followerCountText = findViewById(R.id.followerCountText);
        followButton = findViewById(R.id.followButtonn);
        backButton = findViewById(R.id.backButton);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNav);

        displayNameText.setText("Loading...");
        bioText.setText("Loading...");
    }

    private void setupData() {
        userId = getIntent().getStringExtra("userId");
        displayName = getIntent().getStringExtra("displayName");
        username = getIntent().getStringExtra("username");
        bio = getIntent().getStringExtra("bio");
        followerCount = getIntent().getIntExtra("followerCount", 0);
        isFollowing = getIntent().getBooleanExtra("isFollowing", false);

        Log.d(TAG, "Received userId: " + userId);
        Log.d(TAG, "Received displayName: " + displayName);
        Log.d(TAG, "Received username: " + username);
        Log.d(TAG, "Received bio: " + bio);
        Log.d(TAG, "Received followerCount: " + followerCount);
        Log.d(TAG, "Received isFollowing: " + isFollowing);

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (displayName != null && bio != null) {
            displayNameText.setText(displayName);
            usernameText.setText("@" + username);
            bioText.setText(bio);
            followerCountText.setText(followerCount + " followers");
            updateFollowButton();
        } else {
            fetchProfileFromApi();
        }
    }

    private void fetchProfileFromApi() {
        String authToken = sessionManager.getToken();
        if (authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Authentication token missing", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Authentication token is missing");
            finish();
            return;
        }

        String authHeader = "Bearer " + authToken;
        Call<ProfileResponse> call = apiService.getProfile(authHeader, username);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profile = response.body();
                    displayName = profile.getDisplayName();
                    bio = profile.getBio();
                    followerCount = profile.getFollowerCount();

                    Log.d(TAG, "API displayName: " + displayName);
                    Log.d(TAG, "API bio: " + bio);
                    Log.d(TAG, "API followerCount: " + followerCount);
                    Log.d(TAG, "API isFollowing: " + isFollowing);

                    displayNameText.setText(displayName != null ? displayName : "Unknown User");
                    usernameText.setText("@" + username);
                    bioText.setText(bio != null ? bio : "No bio available");
                    followerCountText.setText(followerCount + " followers");
                    updateFollowButton();
                } else {
                    String errorMessage = "Failed to load profile: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage += "\nDetail: " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    Toast.makeText(ViewProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(ViewProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateFollowButton() {
        if (isFollowing) {
            followButton.setText("Following");
            followButton.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        } else {
            followButton.setText("Follow");
            followButton.setBackgroundTintList(getColorStateList(android.R.color.black));
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        followButton.setOnClickListener(v -> handleFollowClick());
    }

    private void handleFollowClick() {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID not available.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "handleFollowClick: userId is null or empty");
            return;
        }

        String authToken = sessionManager.getToken();
        if (authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Authentication token missing. Please log in again.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "handleFollowClick: Authentication token is missing.");
            return;
        }

        String authHeader = "Bearer " + authToken;
        Log.d(TAG, "Auth Header: " + authHeader);

        if (isFollowing) {
            unfollowUser(authHeader, userId);
        } else {
            followUser(authHeader, userId);
        }
    }

    private void followUser(String authHeader, String targetUserId) {
        Log.d(TAG, "Attempting to follow user: " + targetUserId + " with token: " + authHeader);
        Call<Void> call = apiService.followUser(authHeader, targetUserId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Follow successful. Code: " + response.code());
                    isFollowing = true;
                    followerCount++;
                    followerCountText.setText(followerCount + " followers");
                    updateFollowButton();
                    Toast.makeText(ViewProfileActivity.this, "Now following " + (displayName != null ? displayName : username), Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "Failed to follow: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            errorMessage += "\nDetail: " + errorBodyString;
                            Log.e(TAG, "Follow Error Response (Code " + response.code() + "): " + errorBodyString);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    Toast.makeText(ViewProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Follow Network Failure: " + t.getMessage(), t);
                Toast.makeText(ViewProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void unfollowUser(String authHeader, String targetUserId) {
        Log.d(TAG, "Attempting to unfollow user: " + targetUserId + " with token: " + authHeader);
        Call<Void> call = apiService.unfollowUser(authHeader, targetUserId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Unfollow successful. Code: " + response.code());
                    isFollowing = false;
                    followerCount--;
                    followerCountText.setText(followerCount + " followers");
                    updateFollowButton();
                    Toast.makeText(ViewProfileActivity.this, "Unfollowed " + (displayName != null ? displayName : username), Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "Failed to unfollow: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            errorMessage += "\nDetail: " + errorBodyString;
                            Log.e(TAG, "Unfollow Error Response (Code " + response.code() + "): " + errorBodyString);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    Toast.makeText(ViewProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Unfollow Network Failure: " + t.getMessage(), t);
                Toast.makeText(ViewProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupTabs() {
        ProfileTabsAdapter adapter = new ProfileTabsAdapter(this, userId, username);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Twist");
                            break;
                        case 1:
                            tab.setText("Likes");
                            break;
                        case 2:
                            tab.setText("Replies");
                            break;
                        case 3:
                            tab.setText("Reposts");
                            break;
                    }
                }
        ).attach();
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return true;
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            return true;
        }
        return false;
    }

    public void showFollowers(View view) {
        Toast.makeText(this, "Show followers: " + followerCount, Toast.LENGTH_SHORT).show();
    }

    private static class ProfileTabsAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
        private String userId;
        private String username;

        public ProfileTabsAdapter(androidx.fragment.app.FragmentActivity fragmentActivity, String userId, String username) {
            super(fragmentActivity);
            this.userId = userId;
            this.username = username;
        }

        @Override
        public androidx.fragment.app.Fragment createFragment(int position) {
            Bundle args = new Bundle();
            args.putString("userId", userId);
            args.putString("username", username);

            switch (position) {
                case 0:
                    TwistFragment twistFragment = TwistFragment.newInstance(userId, username);
                    twistFragment.setArguments(args);
                    return twistFragment;
                case 1:
                    LikesFragment likesFragment = LikesFragment.newInstance(userId, username);
                    likesFragment.setArguments(args);
                    return likesFragment;
                case 2:
                    RepliesFragment repliesFragment = RepliesFragment.newInstance(userId, username);
                    repliesFragment.setArguments(args);
                    return repliesFragment;
                case 3:
                    RepostsFragment repostsFragment = RepostsFragment.newInstance(userId, username);
                    repostsFragment.setArguments(args);
                    return repostsFragment;
                default:
                    TwistFragment defaultFragment = TwistFragment.newInstance(userId, username);
                    defaultFragment.setArguments(args);
                    return defaultFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}