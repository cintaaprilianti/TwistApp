package com.example.twist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.model.request.RegisterRequest;
import com.example.twist.model.response.AuthResponse;
import com.example.twist.repository.AuthRepository;
import com.example.twist.util.SessionManager;
import com.example.twist.util.ValidationUtil;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogin;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();
        initViews();
        setListeners();
    }

    private void initComponents() {
        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);
    }

    private void initViews() {
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        clearErrors();

        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        boolean isValid = true;

        if (!ValidationUtil.isValidUsername(username)) {
            editUsername.setError("Username is required");
            isValid = false;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            editEmail.setError("Valid email is required");
            isValid = false;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            editPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (!ValidationUtil.isPasswordMatch(password, confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrors() {
        editUsername.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        editConfirmPassword.setError(null);
    }

    private void registerUser() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        setSignUpButtonState(false, "Creating Account...");

        RegisterRequest request = new RegisterRequest(username, email, password, confirmPassword);
        Call<AuthResponse> call = authRepository.register(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setSignUpButtonState(true, "Sign Up");

                if (response.isSuccessful() && response.body() != null) {
                    handleRegisterSuccess(response.body());
                } else {
                    handleRegisterError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setSignUpButtonState(true, "Sign Up");
                Toast.makeText(SignupActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleRegisterSuccess(AuthResponse response) {
        AuthResponse.User user = response.getUser();
        sessionManager.saveUserSession(
                response.getToken(),
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        Toast.makeText(this, "Welcome " + user.getUsername() + "! Account created.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleRegisterError(Response<AuthResponse> response) {
        String errorMsg = "Registration failed";
        if (response.errorBody() != null) {
            try {
                errorMsg = new JSONObject(response.errorBody().string()).optString("error", errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    private void setSignUpButtonState(boolean enabled, String text) {
        btnSignUp.setEnabled(enabled);
        btnSignUp.setText(text);
    }
}
