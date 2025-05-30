package com.example.twist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.model.request.LoginRequest;
import com.example.twist.model.response.AuthResponse;
import com.example.twist.repository.AuthRepository;
import com.example.twist.util.SessionManager;
import com.example.twist.util.ValidationUtil;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView, signUpTextView;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
        initViews();
        setListeners();
    }

    private void initComponents() {
        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);
    }

    private void initViews() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        forgotPasswordTextView = findViewById(R.id.tvForgotPassword);
        signUpTextView = findViewById(R.id.tvSignUp);
    }

    private void setListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());

        forgotPasswordTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        signUpTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void attemptLogin() {
        clearErrors();

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInputs(username, password)) return;

        performLogin(username, password);
    }

    private void clearErrors() {
        usernameEditText.setError(null);
        passwordEditText.setError(null);
    }

    private boolean validateInputs(String username, String password) {
        boolean isValid = true;
        View focusView = null;

        if (!ValidationUtil.isValidPassword(password)) {
            passwordEditText.setError("Password must be at least 6 characters");
            focusView = passwordEditText;
            isValid = false;
        }

        if (!ValidationUtil.isValidUsername(username)) {
            usernameEditText.setError("Username is required");
            focusView = usernameEditText;
            isValid = false;
        }

        if (focusView != null) focusView.requestFocus();

        return isValid;
    }

    private void performLogin(String username, String password) {
        setLoginButtonState(false, "Logging in...");

        LoginRequest request = new LoginRequest(username, password);
        Call<AuthResponse> call = authRepository.login(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoginButtonState(true, "Login");

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    handleLoginError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoginButtonState(true, "Login");
                Log.e(TAG, "Network failure: " + t.getMessage());
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(AuthResponse response) {
        AuthResponse.User user = response.getUser();
        sessionManager.saveUserSession(
                response.getToken(),
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        Toast.makeText(this, "Welcome back, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleLoginError(Response<AuthResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            JSONObject errorJson = new JSONObject(errorBody);
            String errorMessage = errorJson.optString("error", "Login failed");
            showLoginError(errorMessage);
        } catch (Exception e) {
            showError("Login failed. Please try again.");
        }
    }

    private void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        passwordEditText.setText("");
        usernameEditText.requestFocus();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setLoginButtonState(boolean enabled, String text) {
        loginButton.setEnabled(enabled);
        loginButton.setText(text);
    }
}
