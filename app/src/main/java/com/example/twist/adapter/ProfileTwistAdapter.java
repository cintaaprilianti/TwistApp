package com.example.twist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostResponse;
import com.example.twist.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileTwistAdapter extends RecyclerView.Adapter<ProfileTwistAdapter.TwistViewHolder> {

    private List<PostResponse> twistList = new ArrayList<>();
    private int currentUserId;
    private OnTwistActionListener listener;
    private ApiService apiService;
    private SessionManager sessionManager;
    private Context context;

    public interface OnTwistActionListener {
        void onEditTwist(int postId, String content);
        void onDeleteTwist(int postId);
        void onLikeTwist(int postId, boolean isLiked);
        void onCommentTwist(int postId);
        void onRepostTwist(int postId);
    }

    public ProfileTwistAdapter(Context context) {
        this.context = context;
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.sessionManager = new SessionManager(context);
    }

    public void setOnTwistActionListener(OnTwistActionListener listener) {
        this.listener = listener;
    }

    public void setTwistList(List<PostResponse> twistList) {
        this.twistList = twistList != null ? new ArrayList<>(twistList) : new ArrayList<>();
        Log.d("ProfileTwistAdapter", "Twist list updated, size: " + this.twistList.size());
        notifyDataSetChanged();
    }

    public List<PostResponse> getTwistList() {
        return new ArrayList<>(twistList);
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public TwistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_twist_profile, parent, false);
        return new TwistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TwistViewHolder holder, int position) {
        PostResponse post = twistList.get(position);

        holder.username.setText(post.getUser().getUsername());
        holder.postContent.setText(post.getContent());
        holder.likeCount.setText(String.valueOf(post.getLikeCount()));
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));
        holder.repostCount.setText(String.valueOf(Math.max(0, post.getRepostCount())));
        holder.createdAt.setText(post.getCreatedAt());
        holder.isEdited.setText(post.getIsEdited() ? " (Edited)" : "");
        holder.isPinned.setVisibility(post.getIsPinned() ? View.VISIBLE : View.GONE);

        holder.profileImage.setImageResource(R.drawable.circular_profile_background);

        holder.likeButton.setSelected(post.getIsLiked());

        holder.likeButton.setOnClickListener(v -> toggleLike(post.getId(), post.getIsLiked(), position));

        holder.commentButton.setOnClickListener(v -> {
            Toast.makeText(context, "Comment feature not yet implemented", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onCommentTwist(post.getId());
            }
        });

        holder.repostButton.setOnClickListener(v -> toggleRepost(post.getId(), post.getIsReposted(), position));

        holder.moreButton.setVisibility(post.getUser().getId() == currentUserId ? View.VISIBLE : View.GONE);
        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.moreButton);
            popup.getMenu().add("Edit");
            popup.getMenu().add("Delete");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Edit")) {
                    if (listener != null) {
                        listener.onEditTwist(post.getId(), post.getContent());
                    }
                    return true;
                } else if (item.getTitle().equals("Delete")) {
                    deletePost(post.getId(), position);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void toggleLike(int postId, boolean isLiked, int position) {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(context, "Authentication token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.likePost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    PostResponse post = twistList.get(position);
                    post.setIsLiked(!isLiked);
                    post.setLikeCount(isLiked ? post.getLikeCount() - 1 : post.getLikeCount() + 1);
                    notifyItemChanged(position);
                    if (listener != null) {
                        listener.onLikeTwist(postId, !isLiked);
                    }
                } else {
                    Toast.makeText(context, "Failed to toggle like: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleRepost(int postId, boolean isReposted, int position) {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(context, "Authentication token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.repostPost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    PostResponse post = twistList.get(position);
                    boolean newIsReposted = !isReposted;
                    int newRepostCount = newIsReposted ? post.getRepostCount() + 1 : Math.max(0, post.getRepostCount() - 1);
                    post.setIsReposted(newIsReposted);
                    post.setRepostCount(newRepostCount);
                    notifyItemChanged(position);
                    if (listener != null) {
                        listener.onRepostTwist(postId);
                    }
                } else {
                    Toast.makeText(context, "Failed to repost: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePost(int postId, int position) {
        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(context, "Authentication token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiService.deletePost("Bearer " + token, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    twistList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, twistList.size());
                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onDeleteTwist(postId);
                    }
                } else {
                    String errorMsg = "Failed to delete post: " + response.code();
                    if (response.code() == 404) {
                        errorMsg = "Post not found or not allowed";
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return twistList != null ? twistList.size() : 0;
    }

    public static class TwistViewHolder extends RecyclerView.ViewHolder {
        TextView username, postContent, likeCount, commentCount, repostCount, createdAt, isEdited;
        LinearLayout likeButton, commentButton, repostButton;
        ImageView moreButton, isPinned, profileImage;

        public TwistViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            postContent = itemView.findViewById(R.id.post_content);
            likeCount = itemView.findViewById(R.id.like_count);
            commentCount = itemView.findViewById(R.id.comment_count);
            repostCount = itemView.findViewById(R.id.repost_count);
            createdAt = itemView.findViewById(R.id.created_at);
            isEdited = itemView.findViewById(R.id.is_edited);
            isPinned = itemView.findViewById(R.id.is_pinned);
            profileImage = itemView.findViewById(R.id.profile_image);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            repostButton = itemView.findViewById(R.id.repost_button);
            moreButton = itemView.findViewById(R.id.more_button);
        }
    }
}