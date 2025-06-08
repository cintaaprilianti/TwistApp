package com.example.twist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.model.post.PostResponse;

import java.util.List;

public class TwistAdapter extends RecyclerView.Adapter<TwistAdapter.TwistViewHolder> {

    private List<PostResponse> twistList;

    public void setTwistList(List<PostResponse> twistList) {
        this.twistList = twistList;
        notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return twistList != null ? twistList.size() : 0;
    }

    static class TwistViewHolder extends RecyclerView.ViewHolder {
        TextView username, postContent, likeCount, commentCount, repostCount;

        public TwistViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            postContent = itemView.findViewById(R.id.post_content);
            likeCount = itemView.findViewById(R.id.like_count);
            commentCount = itemView.findViewById(R.id.comment_count);
            repostCount = itemView.findViewById(R.id.repost_count);
        }
    }
}