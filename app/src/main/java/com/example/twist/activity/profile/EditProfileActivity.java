package com.example.twist.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.activity.auth.LoginActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.profile.ProfileResponse;
import com.example.twist.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private ImageView backButton;
    private EditText etDisplayName, etUsername, etBio, etEmail;
    private Button btnSaveChanges;
    private SessionManager sessionManager;
    private ApiService apiService;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);
        currentUsername = getIntent().getStringExtra("username");

        backButton = findViewById(R.id.backButton);
        etDisplayName = findViewById(R.id.etDisplayName);
        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        etEmail = findViewById(R.id.etEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        loadCurrentProfile();

        backButton.setOnClickListener(v -> onBackPressed());

        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadCurrentProfile() {
        String token = sessionManager.getToken();
        if (token != null && currentUsername != null) {
            Call<ProfileResponse> call = apiService.getProfile("Bearer " + token, currentUsername);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileResponse profile = response.body();
                        etDisplayName.setText(profile.getDisplayName());
                        etUsername.setText(profile.getUsername());
                        etBio.setText(profile.getBio());
                        etEmail.setText(profile.getEmail());
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to load profile: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to load profile. Response: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Network error: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "No authentication token or username", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileChanges() {
        String token = sessionManager.getToken();
        if (token != null) {
            String newDisplayName = etDisplayName.getText().toString().trim();
            String newUsername = etUsername.getText().toString().trim();
            String newBio = etBio.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (newDisplayName.isEmpty() || newUsername.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ProfileResponse updatedProfile = new ProfileResponse();
            updatedProfile.setDisplayName(newDisplayName);
            updatedProfile.setUsername(newUsername);
            updatedProfile.setBio(newBio);
            updatedProfile.setEmail(newEmail);

            Call<ProfileResponse> call = apiService.updateProfile("Bearer " + token, currentUsername, updatedProfile);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to update profile. Response: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Network error: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "No authentication token found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}