package com.example.twist.activity.post;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.twist.R;
import com.example.twist.activity.home.HomeActivity;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostPayload;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTwistActivity extends AppCompatActivity {

    private EditText quoteInput;
    private Button postButton;
    private ImageButton backButton;
    private TextView characterCounter;
    private static final int MAX_CHARACTERS = 280;
    private static final String PREFS_NAME = "TwistPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_twist);

        quoteInput = findViewById(R.id.quoteInput);
        postButton = findViewById(R.id.postButton);
        backButton = findViewById(R.id.backButton);
        characterCounter = findViewById(R.id.characterCounter);

        Toolbar toolbar = findViewById(R.id.toolbarContainer);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Aksi untuk Post Button
        postButton.setOnClickListener(v -> {
            String quoteContent = quoteInput.getText().toString().trim();
            if (!quoteContent.isEmpty() && quoteContent.length() <= MAX_CHARACTERS) {
                String token = getTokenFromStorage();
                Log.d("AddTwistActivity", "Token: " + (token != null ? token : "null"));
                Log.d("AddTwistActivity", "Payload: " + new Gson().toJson(new PostPayload(quoteContent)));
                if (token != null) {
                    postQuote(apiService, token, quoteContent);
                } else {
                    Toast.makeText(this, "Silakan login untuk memposting", Toast.LENGTH_SHORT).show();
                }
            } else if (quoteContent.length() > MAX_CHARACTERS) {
                Toast.makeText(this, "Maksimum 280 karakter", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quote tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi untuk Back Button
        backButton.setOnClickListener(v -> onBackPressed());

        // TextWatcher untuk menghitung karakter
        quoteInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                characterCounter.setText(currentLength + "/280");
                if (currentLength > MAX_CHARACTERS) {
                    characterCounter.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    characterCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void postQuote(ApiService apiService, String token, String quoteContent) {
        Call<Void> call = apiService.createPost("Bearer " + token, new PostPayload(quoteContent));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddTwistActivity.this, "Twist berhasil diposting", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddTwistActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("AddTwistActivity", "Error: " + response.code() + ", Body: " + (response.errorBody() != null ? response.errorBody().toString() : "No detail"));
                    Toast.makeText(AddTwistActivity.this, "Gagal memposting twist: Kode " + response.code() + " - " + (response.errorBody() != null ? response.errorBody().toString() : "Tidak ada detail"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AddTwistActivity", "Network error: " + t.getMessage());
                Toast.makeText(AddTwistActivity.this, "Koneksi bermasalah: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getTokenFromStorage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("auth_token", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}