package com.example.twist.activity.auth;

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

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnSaveChanges;
    private ImageView closeButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        closeButton = findViewById(R.id.closeButton);
        progressBar = findViewById(R.id.progressBar);

        closeButton.setOnClickListener(v -> finish());

        btnSaveChanges.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                return;
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            String token = getIntent().getStringExtra("resetToken");
            if (token == null) {
                Toast.makeText(this, "Invalid reset token", Toast.LENGTH_SHORT).show();
                return;
            }

            sendResetPasswordRequest(token, newPassword, confirmPassword);
        });
    }

    private void sendResetPasswordRequest(String token, String password, String confirmPassword) {
        progressBar.setVisibility(View.VISIBLE);
        btnSaveChanges.setEnabled(false);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AuthRequest request = new AuthRequest(token, password, confirmPassword); // Gunakan constructor untuk reset password
        Call<AuthResponse> call = apiService.resetPassword(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSaveChanges.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(com.example.twist.activity.auth.ResetPasswordActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(com.example.twist.activity.auth.ResetPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSaveChanges.setEnabled(true);
                Toast.makeText(com.example.twist.activity.auth.ResetPasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}