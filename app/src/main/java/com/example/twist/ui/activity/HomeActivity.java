package com.example.twist.ui.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.twist.R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(navListener);
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_home) {
                        Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.nav_search) {
                        Toast.makeText(HomeActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.nav_add) {
                        Toast.makeText(HomeActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.nav_likes) {
                        Toast.makeText(HomeActivity.this, "Likes", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    return false;
                }
            };
}