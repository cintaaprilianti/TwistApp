package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.post.PostResponse;

import java.util.List;

public class TwistAdapter extends RecyclerView.Adapter<TwistAdapter.TwistViewHolder> {

    private List<PostResponse> twistList;
    private OnItemInteractionListener listener;

    // Interface untuk callback
    public interface OnItemInteractionListener {
        void onLikeClicked(int postId);
        void onRepostClicked(int postId);
        void onCommentClicked(int postId);
        void onViewCommentsClicked(int postId);
    }

    public void setTwistList(List<PostResponse> twistList) {
        this.twistList = twistList;
        notifyDataSetChanged();
    }

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public TwistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_twist, parent, false);
        return new TwistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TwistViewHolder holder, int position) {
        PostResponse post = twistList.get(position);
        holder.username.setText(post.getUser().getUsername());
        holder.postContent.setText(post.getContent());
        holder.likeCount.setText(String.valueOf(post.getLikeCount()));
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));
        holder.repostCount.setText(String.valueOf(post.getRepostCount()));

        // Set status like dan repost (opsional, bisa diubah ikon sesuai isLiked/isReposted)
        // Misalnya, ubah ikon jika isLiked true
        // holder.likeIcon.setImageResource(post.getIsLiked() ? R.drawable.liked : R.drawable.love);

        // Set klik listener
        holder.likeButton.setOnClickListener(v -> {
            if (listener != null) listener.onLikeClicked(post.getId());
        });
        holder.repostButton.setOnClickListener(v -> {
            if (listener != null) listener.onRepostClicked(post.getId());
        });
        holder.commentButton.setOnClickListener(v -> {
            if (listener != null) listener.onCommentClicked(post.getId());
        });
        holder.viewCommentsButton.setOnClickListener(v -> {
            if (listener != null) listener.onViewCommentsClicked(post.getId());
        });
    }

    @Override
    public int getItemCount() {
        return twistList != null ? twistList.size() : 0;
    }

    static class TwistViewHolder extends RecyclerView.ViewHolder {
        TextView username, postContent, likeCount, commentCount, repostCount, viewCommentsButton;
        View likeButton, repostButton, commentButton;

        public TwistViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            postContent = itemView.findViewById(R.id.post_content);
            likeCount = itemView.findViewById(R.id.like_count);
            commentCount = itemView.findViewById(R.id.comment_count);
            repostCount = itemView.findViewById(R.id.repost_count);
            viewCommentsButton = itemView.findViewById(R.id.view_comments_button);
            likeButton = itemView.findViewById(R.id.like_button);
            repostButton = itemView.findViewById(R.id.repost_button); // Perbaikan di sini
            commentButton = itemView.findViewById(R.id.comment_button); // Perbaikan di sini
        }
    }
}