package com.example.twist.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.activity.post.AddTwistActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.adapter.TwistAdapter;
import com.example.twist.model.post.PostResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private RecyclerView twistRecyclerView;
    private TwistAdapter twistAdapter;
    private TextView welcomeText;
    private EditText searchBar;
    private BottomNavigationView bottomNav;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi komponen
        twistRecyclerView = findViewById(R.id.twist_recycler_view);
        welcomeText = findViewById(R.id.welcomeText);
        searchBar = findViewById(R.id.search_bar);
        bottomNav = findViewById(R.id.bottomNav);

        // Set up RecyclerView
        twistAdapter = new TwistAdapter();
        twistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        twistRecyclerView.setAdapter(twistAdapter);

        // Inisialisasi Presenter dengan ApiClient
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        presenter = new HomePresenter(this, apiService, getTokenFromStorage());

        // Set up Bottom Navigation
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Muat data
        presenter.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data saat kembali ke HomeActivity
        presenter.loadData();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            return true;
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddTwistActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Fitur Profile belum diimplementasikan", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private String getTokenFromStorage() {
        // Implementasi logika untuk mengambil token dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("TwistPrefs", MODE_PRIVATE);
        return prefs.getString("auth_token", null); // Ganti dengan key token Anda
    }

    @Override
    public void showWelcomeMessage() {
        welcomeText.setVisibility(View.VISIBLE);
        twistRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideWelcomeMessage() {
        welcomeText.setVisibility(View.GONE);
        twistRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTwistList(List<PostResponse> posts) {
        twistAdapter.setTwistList(posts);
        twistAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        // Implementasi loading (misalnya ProgressBar) jika diperlukan
    }

    @Override
    public void hideLoading() {
        // Implementasi hide loading jika diperlukan
    }
}