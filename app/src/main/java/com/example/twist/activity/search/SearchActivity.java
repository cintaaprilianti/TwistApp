package com.example.twist.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.activity.post.AddTwistActivity;
import com.example.twist.activity.profile.ProfileActivity;
import com.example.twist.activity.profile.ViewProfileActivity;
import com.example.twist.adapter.UserSearchAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.search.SearchUserResponse;
import com.example.twist.model.search.User;
import com.example.twist.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView userRecyclerView;
    private BottomNavigationView bottomNav;
    private UserSearchAdapter userAdapter;
    private List<User> filteredUserList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private Toast currentToast;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SessionManager sessionManager = new SessionManager(this);
        token = sessionManager.getToken();

        initViews();
        setupRecyclerView();
        setupSearchFunctionality();
        setupBottomNavigation();
    }

    private void initViews() {
        searchBar = findViewById(R.id.search_bar);
        userRecyclerView = findViewById(R.id.user_recycler_view);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupRecyclerView() {
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
                if (searchRunnable != null) handler.removeCallbacks(searchRunnable);
                searchRunnable = () -> searchUsersFromApi(s.toString().trim());
                handler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return true;
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
            return true;
        }
        return false;
    }

    private void searchUsersFromApi(String query) {
        if (query.isEmpty()) {
            filteredUserList.clear();
            userAdapter.notifyDataSetChanged();
            return;
        }

        if (token == null || token.isEmpty()) {
            showToastOnce("Token tidak ditemukan. Silakan login ulang.");
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<SearchUserResponse> call = api.searchUsers("Bearer " + token, query, "users");

        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    filteredUserList.clear();
                    for (User user : response.body().getResults()) {
                        user.setProfileImageResource(R.drawable.profile);
                        filteredUserList.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                } else {
                    showToastOnce("Gagal memuat hasil: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                showToastOnce("Koneksi gagal: " + t.getMessage());
            }
        });
    }

    private void showToastOnce(String message) {
        if (currentToast != null) currentToast.cancel();
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    private void onUserClicked(User user) {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("userId", user.getId());
        intent.putExtra("username", user.getUsername());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}