package com.example.twist.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        btnSignUp.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthRequest request = new AuthRequest(username, password, email, confirmPassword);

            apiService.register(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(com.example.twist.activity.auth.SignupActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(com.example.twist.activity.auth.SignupActivity.this, com.example.twist.activity.auth.LoginActivity.class));
                        finish();
                    } else {
                        try {
                            String errorMessage = parseErrorMessage(response);
                            if (errorMessage != null && errorMessage.contains("sudah digunakan")) {
                                Toast.makeText(com.example.twist.activity.auth.SignupActivity.this, "Username atau email sudah digunakan", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(com.example.twist.activity.auth.SignupActivity.this, "Registrasi gagal: " + (errorMessage != null ? errorMessage : response.message()), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(com.example.twist.activity.auth.SignupActivity.this, "Registrasi gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(com.example.twist.activity.auth.SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(com.example.twist.activity.auth.SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private String parseErrorMessage(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                return new Gson().fromJson(response.errorBody().string(), com.example.twist.activity.auth.SignupActivity.ErrorResponse.class).getError();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    // Model sederhana untuk parsing error response
    private static class ErrorResponse {
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}