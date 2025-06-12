package com.example.twist.model.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.activity.auth.LoginActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private ImageButton backButton;
    private Button logoutButton, deleteAccountButton;
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        backButton = findViewById(R.id.back_button);
        logoutButton = findViewById(R.id.logout_button);
        deleteAccountButton = findViewById(R.id.delete_account_button);

        backButton.setOnClickListener(v -> onBackPressed());

        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(SettingsActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        deleteAccountButton.setOnClickListener(v -> showDeleteAccountConfirmation());
    }

    private void showDeleteAccountConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to permanently delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .setCancelable(true)
                .show();
    }

    private void deleteAccount() {
        String token = sessionManager.getToken();
        if (token == null) {
            Log.e(TAG, "Authentication token not found");
            Toast.makeText(this, "Authentication token not found", Toast.LENGTH_SHORT).show();
            navigateToLogin();
            return;
        }

        Log.d(TAG, "Deleting account with token: " + token.substring(0, Math.min(token.length(), 10)) + "...");
        deleteAccountButton.setEnabled(false);
        Call<Void> call = apiService.deleteAccount("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                deleteAccountButton.setEnabled(true);
                Log.d(TAG, "Delete account response code: " + response.code());
                if (response.isSuccessful()) {
                    Log.d(TAG, "Account deleted successfully");
                    sessionManager.clearSession();
                    Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    String errorMessage = "Failed to delete account: " + response.code();
                    if (response.code() == 401) {
                        errorMessage = "Invalid token. Please log in again.";
                        Log.e(TAG, "Invalid token (401)");
                        navigateToLogin();
                    } else if (response.code() == 404) {
                        errorMessage = "User not found.";
                        Log.e(TAG, "User not found (404)");
                    }
                    try {
                        if (response.errorBody() != null) {
                            errorMessage += ". Detail: " + response.errorBody().string();
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(SettingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                deleteAccountButton.setEnabled(true);
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(SettingsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}