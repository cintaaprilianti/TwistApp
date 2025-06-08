package com.example.twist.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.twist.R;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnContinue;
    private ImageView closeButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnContinue = findViewById(R.id.btnContinue);
        closeButton = findViewById(R.id.closeButton);
        progressBar = findViewById(R.id.progressBar);

        closeButton.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan email Anda", Toast.LENGTH_SHORT).show();
                return;
            }
            sendForgotPasswordRequest(email);
        });
    }

    private void sendForgotPasswordRequest(String email) {
        progressBar.setVisibility(View.VISIBLE);
        btnContinue.setEnabled(false);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AuthRequest request = new AuthRequest(email); // Gunakan constructor untuk forgot password
        Call<AuthResponse> call = apiService.forgotPassword(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnContinue.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(com.example.twist.activity.auth.ForgotPasswordActivity.this, authResponse.getMessage() + "\nReset Link: " + authResponse.getResetLink(), Toast.LENGTH_LONG).show();
                    String token = authResponse.getResetToken();
                    if (token != null) {
                        startResetPasswordActivity(token);
                    }
                } else {
                    Toast.makeText(com.example.twist.activity.auth.ForgotPasswordActivity.this, "Failed to send reset request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnContinue.setEnabled(true);
                Toast.makeText(com.example.twist.activity.auth.ForgotPasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startResetPasswordActivity(String token) {
        Intent intent = new Intent(com.example.twist.activity.auth.ForgotPasswordActivity.this, ResetPasswordActivity.class);
        intent.putExtra("resetToken", token);
        startActivity(intent);
        finish();
    }
}