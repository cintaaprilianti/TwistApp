package com.example.twist.model.search;

import java.util.List;

public class SearchUserResponse {
    private String query;
    private String type;
    private int count;
    private List<User> results;

    public String getQuery() { return query; }
    public String getType() { return type; }
    public int getCount() { return count; }
    public List<User> getResults() { return results; }
}