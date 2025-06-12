package com.example.twist.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.activity.post.AddTwistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.twist.R;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.activity.profile.ProfileActivity;
import com.example.twist.adapter.UserSearchAdapter;
import com.example.twist.model.search.User;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView userRecyclerView;
    private BottomNavigationView bottomNav;
    private UserSearchAdapter userAdapter;
    private List<User> userList;
    private List<User> filteredUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupRecyclerView();
        setupSearchFunctionality();
        setupBottomNavigation();
        loadUsers();
    }

    private void initViews() {
        searchBar = findViewById(R.id.search_bar);
        userRecyclerView = findViewById(R.id.user_recycler_view);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();

        userAdapter = new UserSearchAdapter(filteredUserList, this::onUserClicked);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userAdapter);
    }

    private void setupSearchFunctionality() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBottomNavigation() {
        // Set up Bottom Navigation - sama seperti di HomeActivity
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return true;
        } else if (id == R.id.nav_add) {
            // Handle add action - sama seperti di HomeActivity
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
            return true;
        }
        return false;
    }

    private void loadUsers() {
        // Sample data - replace with actual data loading from API/Database
        userList.clear();
        userList.add(new User("1", "zaynmalik", "63.8M followers", R.drawable.profile));
        userList.add(new User("2", "alychlt", "5.1M followers", R.drawable.profile));
        userList.add(new User("3", "najahkarimah", "1,025 followers", R.drawable.profile));
        userList.add(new User("4", "rahmarajti_dita", "752 followers", R.drawable.profile));
        userList.add(new User("5", "cinthe", "371 followers", R.drawable.profile));

        // Initially show all users
        filteredUserList.clear();
        filteredUserList.addAll(userList);
        userAdapter.notifyDataSetChanged();
    }

    private void filterUsers(String query) {
        filteredUserList.clear();

        if (query.isEmpty()) {
            filteredUserList.addAll(userList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (User user : userList) {
                if (user.getUsername().toLowerCase().contains(lowerCaseQuery)) {
                    filteredUserList.add(user);
                }
            }
        }

        userAdapter.notifyDataSetChanged();
    }

    private void onUserClicked(User user) {
        // Handle user click - navigate to user profile
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user_id", user.getId());
        intent.putExtra("username", user.getUsername());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to home
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}