package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.post.PostResponse;

import java.util.List;

public class ProfileTwistAdapter extends RecyclerView.Adapter<ProfileTwistAdapter.TwistViewHolder> {

    private List<PostResponse> twistList;
    private int currentUserId;
    private OnTwistActionListener onTwistActionListener;

    public interface OnTwistActionListener {
        void onEditTwist(int postId);
        void onDeleteTwist(int postId);
    }

    public void setOnTwistActionListener(OnTwistActionListener listener) {
        this.onTwistActionListener = listener;
    }

    public void setTwistList(List<PostResponse> twistList) {
        this.twistList = twistList;
        notifyDataSetChanged();
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

        // Set data
        holder.username.setText(post.getUser().getUsername());
        holder.postContent.setText(post.getContent());
        holder.likeCount.setText(String.valueOf(post.getLikeCount()));
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));
        holder.repostCount.setText(String.valueOf(post.getRepostCount()));

        // Atur visibilitas tombol more berdasarkan currentUserId
        holder.moreButton.setVisibility(post.getUser().getId() == currentUserId ? View.VISIBLE : View.GONE);

        // Aksi tombol more
        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.moreButton);
            popupMenu.getMenu().add("Edit");
            popupMenu.getMenu().add("Delete");
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Edit")) {
                    if (onTwistActionListener != null) {
                        onTwistActionListener.onEditTwist(post.getId());
                    }
                    return true;
                } else if (item.getTitle().equals("Delete")) {
                    if (onTwistActionListener != null) {
                        onTwistActionListener.onDeleteTwist(post.getId());
                    }
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return twistList != null ? twistList.size() : 0;
    }

    public static class TwistViewHolder extends RecyclerView.ViewHolder {
        TextView username, postContent, likeCount, commentCount, repostCount;
        LinearLayout likeButton, commentButton, repostButton;
        ImageView moreButton;

        public TwistViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            postContent = itemView.findViewById(R.id.post_content);
            likeCount = itemView.findViewById(R.id.like_count);
            commentCount = itemView.findViewById(R.id.comment_count);
            repostCount = itemView.findViewById(R.id.repost_count);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            repostButton = itemView.findViewById(R.id.repost_button);
            moreButton = itemView.findViewById(R.id.more_button);
        }
    }
}