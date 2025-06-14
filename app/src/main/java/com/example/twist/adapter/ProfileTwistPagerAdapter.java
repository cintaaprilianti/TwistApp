package com.example.twist.adapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.twist.fragment.ProfilePostsFragment;
import com.example.twist.fragment.ProfileLikesFragment;
import com.example.twist.fragment.ProfileRepliesFragment;
import com.example.twist.fragment.ProfileRepostsFragment;

public class ProfileTwistPagerAdapter extends FragmentStateAdapter {

    private int currentUserId;
    private String username;

    public ProfileTwistPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setUsername(String username) {
        this.username = username;
        Log.d("ProfileTwistPagerAdapter", "Username set: " + username);
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
        Log.d("ProfileTwistPagerAdapter", "CurrentUserId set: " + currentUserId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putInt("userId", currentUserId);
        Log.d("ProfileTwistPagerAdapter", "Creating fragment for position: " + position + ", username: " + username + ", userId: " + currentUserId);

        switch (position) {
            case 0:
                ProfilePostsFragment postsFragment = new ProfilePostsFragment();
                postsFragment.setArguments(args);
                return postsFragment;
            case 1:
                ProfileLikesFragment likesFragment = new ProfileLikesFragment();
                likesFragment.setArguments(args);
                return likesFragment;
            case 2:
                ProfileRepliesFragment repliesFragment = new ProfileRepliesFragment();
                repliesFragment.setArguments(args);
                return repliesFragment;
            case 3:
                ProfileRepostsFragment repostsFragment = new ProfileRepostsFragment();
                repostsFragment.setArguments(args);
                return repostsFragment;
            default:
                ProfilePostsFragment defaultFragment = new ProfilePostsFragment();
                defaultFragment.setArguments(args);
                return defaultFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Posts, Likes, Replies, Reposts
    }
}