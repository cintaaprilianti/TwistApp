package com.example.twist.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.twist.R;
import com.example.twist.activity.auth.LoginActivity;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.adapter.ProfileTwistPagerAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.profile.ProfileResponse;
import com.example.twist.model.profile.SettingsActivity;
import com.example.twist.util.SessionManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private TextView displayName, username, bio, followersCount, followingCount;
    private Button editProfileButton;
    private ImageButton menuButton, backButton;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ApiService apiService;
    private String currentUsername;
    private int currentUserId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        displayName = findViewById(R.id.display_name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        editProfileButton = findViewById(R.id.edit_profile_button);
        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        apiService = ApiClient.getClient().create(ApiService.class);
        currentUsername = getIntent().getStringExtra("username");
        currentUserId = getIntent().getIntExtra("userId", -1);

        Log.d(TAG, "Current Username: " + currentUsername);
        Log.d(TAG, "Current UserId: " + currentUserId);

        if (currentUsername == null) {
            Toast.makeText(this, "Username tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProfile();
        setupViewPager();

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("username", currentUsername);
            startActivity(intent);
        });

        menuButton.setOnClickListener(v -> showSettingsMenu());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadProfile() {
        String token = sessionManager.getToken();
        Log.d(TAG, "Token: " + (token != null ? token.substring(0, 10) + "..." : "null"));

        if (token != null) {
            Call<ProfileResponse> call = apiService.getProfile("Bearer " + token, currentUsername);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    Log.d(TAG, "API Response Code: " + response.code());
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileResponse profile = response.body();
                        displayName.setText(profile.getDisplayName() != null ? profile.getDisplayName() : profile.getUsername());
                        username.setText("@" + profile.getUsername());
                        bio.setText(profile.getBio() != null ? profile.getBio() : "Belum ada bio");
                        followersCount.setText(String.valueOf(profile.getFollowerCount()) + " Followers");
                        followingCount.setText(String.valueOf(profile.getFollowingCount()) + " Following");
                        ProfileTwistPagerAdapter pagerAdapter = (ProfileTwistPagerAdapter) viewPager.getAdapter();
                        if (pagerAdapter != null) {
                            pagerAdapter.setCurrentUserId(currentUserId);
                            pagerAdapter.setUsername(currentUsername);
                            pagerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String errorMsg = "Gagal memuat profil: " + response.code();
                        if (response.code() == 404) {
                            errorMsg = "Profil tidak ditemukan";
                        }
                        Toast.makeText(ProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        try {
                            Log.e(TAG, "Gagal memuat profil. Response: " + (response.errorBody() != null ? response.errorBody().string() : "Tidak ada error body"));
                        } catch (Exception e) {
                            Log.e(TAG, "Error membaca error body", e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error jaringan: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Token autentikasi tidak ditemukan", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void setupViewPager() {
        ProfileTwistPagerAdapter pagerAdapter = new ProfileTwistPagerAdapter(this);
        pagerAdapter.setUsername(currentUsername);
        pagerAdapter.setCurrentUserId(currentUserId);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Posts");
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
                }).attach();
    }

    private void showSettingsMenu() {
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(this, menuButton);
        popupMenu.getMenu().add("Settings");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Settings")) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}