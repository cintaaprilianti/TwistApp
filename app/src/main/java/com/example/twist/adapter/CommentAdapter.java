package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.post.CommentResponse;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentResponse> commentList = new ArrayList<>();

    public void setCommentList(List<CommentResponse> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentResponse comment = commentList.get(position);
        holder.usernameText.setText(comment.getUsername());
        holder.contentText.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, contentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.commentUsernameText);
            contentText = itemView.findViewById(R.id.commentContentText);
        }
    }
}