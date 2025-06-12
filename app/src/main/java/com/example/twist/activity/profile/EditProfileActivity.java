package com.example.twist.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.example.twist.model.profile.UpdateProfileRequest;
import com.example.twist.util.SessionManager;

import java.io.IOException;

import okhttp3.ResponseBody;
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

        if (currentUsername == null) {
            Toast.makeText(this, "Username tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
                        String errorMsg = "Gagal memuat profil: " + response.code();
                        try {
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                                errorMsg += ". Detail: " + errorBody.string();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Error membaca body error", e);
                        }
                        Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, errorMsg);
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error jaringan: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Token autentikasi atau username tidak ada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveProfileChanges() {
        String token = sessionManager.getToken();
        if (token != null) {
            String newDisplayName = etDisplayName.getText().toString().trim();
            String newUsername = etUsername.getText().toString().trim();
            String newBio = etBio.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            // Validate inputs
            if (newDisplayName.isEmpty()) {
                etDisplayName.setError("Display name tidak boleh kosong");
                return;
            }
            if (newUsername.isEmpty()) {
                etUsername.setError("Username tidak boleh kosong");
                return;
            }
            if (!newUsername.matches("^[a-zA-Z0-9_]+$")) {
                etUsername.setError("Username hanya boleh berisi huruf, angka, dan underscore");
                return;
            }
            if (newEmail.isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                etEmail.setError("Email tidak valid");
                return;
            }

            Log.d(TAG, "Memperbarui profil untuk username: " + newUsername);
            UpdateProfileRequest updatedProfile = new UpdateProfileRequest(newDisplayName, newUsername, newBio, newEmail);

            Call<ProfileResponse> call = apiService.updateProfile("Bearer " + token, updatedProfile);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(EditProfileActivity.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sessionManager.saveUsername(newUsername); // Update username in session
                        setResult(RESULT_OK); // Set result to indicate success
                        finish(); // Close activity
                    } else {
                        String errorMessage = "Gagal memperbarui profil: " + response.code();
                        if (response.code() == 401) {
                            errorMessage = "Token tidak valid. Silakan login kembali.";
                            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else if (response.code() == 409) {
                            errorMessage = "Username atau email sudah digunakan.";
                        }
                        try {
                            ResponseBody errorBody = response.errorBody();
                            if (errorBody != null) {
                                errorMessage += ". Detail: " + errorBody.string();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Error membaca body error", e);
                        }
                        Toast.makeText(EditProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error jaringan: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Token autentikasi tidak ditemukan", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}