package com.example.twist.activity.profile;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.twist.fragment.LikesFragment;
import com.example.twist.fragment.RepliesFragment;
import com.example.twist.fragment.RepostsFragment;
import com.example.twist.fragment.TwistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView displayNameText;
    private TextView usernameText;
    private TextView bioText;
    private TextView followerCountText;
    private Button followButton;
    private ImageButton backButton;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;

    // Data user yang akan ditampilkan
    private String userId;
    private String displayName;
    private String username;
    private String bio;
    private int followerCount;
    private boolean isFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

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
    }

    private void setupData() {
        // Ambil data user dari intent
        userId = getIntent().getStringExtra("userId");
        displayName = getIntent().getStringExtra("displayName");
        username = getIntent().getStringExtra("username");
        bio = getIntent().getStringExtra("bio");
        followerCount = getIntent().getIntExtra("followerCount", 0);
        isFollowing = getIntent().getBooleanExtra("isFollowing", false);

        // Set data ke views
        displayNameText.setText(displayName != null ? displayName : "Unknown User");
        usernameText.setText(username != null ? "@" + username : "@unknown");
        bioText.setText(bio != null ? bio : "No bio available");
        followerCountText.setText(followerCount + " followers");

        // Set status follow button
        updateFollowButton();
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
        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Follow button
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFollowClick();
            }
        });
    }

    private void handleFollowClick() {
        if (isFollowing) {
            // Unfollow user
            unfollowUser();
        } else {
            // Follow user
            followUser();
        }
    }

    private void followUser() {
        // TODO: Implement API call to follow user
        // For now, just simulate the action
        isFollowing = true;
        followerCount++;
        followerCountText.setText(followerCount + " followers");
        updateFollowButton();
        Toast.makeText(this, "Now following " + displayName, Toast.LENGTH_SHORT).show();
    }

    private void unfollowUser() {
        // TODO: Implement API call to unfollow user
        // For now, just simulate the action
        isFollowing = false;
        followerCount--;
        followerCountText.setText(followerCount + " followers");
        updateFollowButton();
        Toast.makeText(this, "Unfollowed " + displayName, Toast.LENGTH_SHORT).show();
    }

    private void setupTabs() {
        // Setup ViewPager2 dengan adapter
        ProfileTabsAdapter adapter = new ProfileTabsAdapter(this, userId);
        viewPager.setAdapter(adapter);

        // Setup TabLayout dengan ViewPager2
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
        // Set profile as selected since we're in profile activity
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish(); // Close current activity
            return true;
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            // Already in profile, do nothing or show current user's profile
            return true;
        }
        return false;
    }

    public void showFollowers(View view) {
        // TODO: Implement show followers functionality
        Toast.makeText(this, "Show followers: " + followerCount, Toast.LENGTH_SHORT).show();
    }

    // Inner class untuk adapter tabs (jika belum ada)
    private static class ProfileTabsAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
        private String userId;

        public ProfileTabsAdapter(androidx.fragment.app.FragmentActivity fragmentActivity, String userId) {
            super(fragmentActivity);
            this.userId = userId;
        }

        @Override
        public androidx.fragment.app.Fragment createFragment(int position) {
            // TODO: Return appropriate fragments for each tab
            // For now, return empty fragments
            switch (position) {
                case 0:
                    return new TwistFragment(); // Fragment untuk Twist
                case 1:
                    return new LikesFragment(); // Fragment untuk Likes
                case 2:
                    return new RepliesFragment(); // Fragment untuk Replies
                case 3:
                    return new RepostsFragment(); // Fragment untuk Reposts
                default:
                    return new TwistFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}