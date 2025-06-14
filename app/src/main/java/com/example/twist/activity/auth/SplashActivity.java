package com.example.twist.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.util.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private static final int DURASI_SPLASH = 2000;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionManager.getToken() != null && !sessionManager.getToken().isEmpty()) {
                Log.d(TAG, "Token found, redirecting to HomeActivity");
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Log.d(TAG, "No token found, redirecting to LoginActivity");
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            finish();
        }, DURASI_SPLASH);
    }
}