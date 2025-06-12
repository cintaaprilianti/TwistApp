package com.example.twist.model.profile;

import com.example.twist.model.post.PostResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfilePostsResponse {
    @SerializedName("posts")
    private List<PostResponse> posts;

    @SerializedName("pagination")
    private Pagination pagination;

    @SerializedName("meta")
    private Meta meta;

    public List<PostResponse> getPosts() {
        return posts;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public static class Pagination {
        @SerializedName("limit")
        private int limit;

        @SerializedName("offset")
        private int offset;

        @SerializedName("count")
        private int count;

        @SerializedName("hasMore")
        private boolean hasMore;

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }

        public int getCount() {
            return count;
        }

        public boolean isHasMore() {
            return hasMore;
        }
    }

    public static class Meta {
        @SerializedName("tab")
        private String tab;

        @SerializedName("username")
        private String username;

        public String getTab() {
            return tab;
        }

        public String getUsername() {
            return username;
        }
    }
}