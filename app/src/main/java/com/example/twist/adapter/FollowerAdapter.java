package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.profile.ProfileResponse;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    private List<ProfileResponse> followers;

    public void setFollowers(List<ProfileResponse> followers) {
        this.followers = followers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder, int position) {
        ProfileResponse follower = followers.get(position);
        holder.displayNameText.setText(follower.getDisplayName() != null ? follower.getDisplayName() : follower.getUsername());
        holder.usernameText.setText("@" + follower.getUsername());
        holder.bioText.setText(follower.getBio());
        holder.followerCountText.setText(follower.getFollowerCount() + " Followers");
    }

    @Override
    public int getItemCount() {
        return followers != null ? followers.size() : 0;
    }

    static class FollowerViewHolder extends RecyclerView.ViewHolder {
        TextView displayNameText, usernameText, bioText, followerCountText;

        public FollowerViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameText = itemView.findViewById(R.id.followerDisplayName);
            usernameText = itemView.findViewById(R.id.followerUsername);
            bioText = itemView.findViewById(R.id.followerBio);
            followerCountText = itemView.findViewById(R.id.followerCount);
        }
    }
}