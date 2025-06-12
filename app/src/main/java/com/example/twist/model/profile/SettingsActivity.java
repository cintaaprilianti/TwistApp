package com.example.twist.model.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.activity.auth.LoginActivity;
import com.example.twist.util.SessionManager;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button logoutButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sessionManager = new SessionManager(this);

        backButton = findViewById(R.id.back_button);
        logoutButton = findViewById(R.id.logout_button);

        backButton.setOnClickListener(v -> onBackPressed());

        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(SettingsActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}