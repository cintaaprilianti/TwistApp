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
import com.example.twist.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> login());

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<AuthResponse> call = apiService.login(new AuthRequest(username, password));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveToken(response.body().getToken());
                    Toast.makeText(com.example.twist.activity.auth.LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(com.example.twist.activity.auth.LoginActivity.this, com.example.twist.activity.home.HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(com.example.twist.activity.auth.LoginActivity.this, "Login gagal. Periksa kembali kredensial Anda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(com.example.twist.activity.auth.LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
