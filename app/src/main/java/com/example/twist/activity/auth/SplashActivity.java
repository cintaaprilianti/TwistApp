package com.example.twist.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;

public class SplashActivity extends AppCompatActivity {
    private static final int DURASI_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(com.example.twist.activity.auth.SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURASI_SPLASH);
    }
}