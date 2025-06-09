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
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.CreateCommentRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private EditText commentInput;
    private Button sendButton;
    private ImageButton backButton;
    private TextView characterCounter;
    private static final int MAX_CHARACTERS = 280;
    private static final String PREFS_NAME = "TwistPrefs";
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Inisialisasi komponen
        commentInput = findViewById(R.id.comment_input);
        sendButton = findViewById(R.id.send_button);
        backButton = findViewById(R.id.back_button);
        characterCounter = findViewById(R.id.character_counter);

        // Ambil postId dari intent
        postId = getIntent().getIntExtra("postId", -1);
        if (postId == -1) {
            Toast.makeText(this, "Post tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Aksi untuk Send Button
        sendButton.setOnClickListener(v -> {
            String commentContent = commentInput.getText().toString().trim();
            if (!commentContent.isEmpty() && commentContent.length() <= MAX_CHARACTERS) {
                String token = getTokenFromStorage();
                if (token != null) {
                    sendComment(apiService, token, commentContent);
                } else {
                    Toast.makeText(this, "Silakan login untuk mengirim komentar", Toast.LENGTH_SHORT).show();
                }
            } else if (commentContent.length() > MAX_CHARACTERS) {
                Toast.makeText(this, "Maksimum 280 karakter", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi untuk Back Button
        backButton.setOnClickListener(v -> onBackPressed());

        // TextWatcher untuk menghitung karakter
        commentInput.addTextChangedListener(new TextWatcher() {
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

    private void sendComment(ApiService apiService, String token, String commentContent) {
        CreateCommentRequest request = new CreateCommentRequest(commentContent);
        Call<Void> call = apiService.addComment("Bearer " + token, postId, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CommentActivity", "Comment sent successfully for postId: " + postId);
                    Toast.makeText(CommentActivity.this, "Komentar berhasil dikirim", Toast.LENGTH_SHORT).show();
                    // Kembali ke CommentListActivity dan segarkan
                    Intent intent = new Intent(CommentActivity.this, CommentListActivity.class);
                    intent.putExtra("postId", postId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("CommentActivity", "Error: " + response.code() + ", Body: " + (response.errorBody() != null ? response.errorBody().toString() : "No detail"));
                    Toast.makeText(CommentActivity.this, "Gagal mengirim komentar: Kode " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CommentActivity", "Network error: " + t.getMessage());
                Toast.makeText(CommentActivity.this, "Koneksi bermasalah: " + t.getMessage(), Toast.LENGTH_LONG).show();
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