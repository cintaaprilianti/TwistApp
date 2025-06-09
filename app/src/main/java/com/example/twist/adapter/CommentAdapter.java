package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.post.CommentResponse;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentResponse> commentList;

    public void setCommentList(List<CommentResponse> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentResponse comment = commentList.get(position);
        holder.username.setText(comment.getUser().getUsername());
        holder.commentContent.setText(comment.getContent());
        holder.createdAt.setText(comment.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username, commentContent, createdAt;

        public CommentViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.comment_username);
            commentContent = itemView.findViewById(R.id.comment_content);
            createdAt = itemView.findViewById(R.id.comment_created_at);
        }
    }
}