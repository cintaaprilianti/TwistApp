package com.example.twist.activity.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.twist.R;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostPayload;
import com.example.twist.model.post.PostResponse;
import com.example.twist.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {

    private EditText contentEditText;
    private Button saveButton;
    private ApiService apiService;
    private SessionManager sessionManager;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        contentEditText = findViewById(R.id.content_edit_text);
        saveButton = findViewById(R.id.save_button);
        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        postId = getIntent().getIntExtra("postId", -1);
        String content = getIntent().getStringExtra("content");

        if (postId == -1) {
            Toast.makeText(this, "ID postingan tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        contentEditText.setText(content);

        saveButton.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        String content = contentEditText.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "Konten tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(this, "Token autentikasi tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PostPayload payload = new PostPayload(content);
        Call<PostResponse> call = apiService.updatePost("Bearer " + token, postId, payload);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Postingan diperbarui", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String errorMsg = "Gagal memperbarui postingan: " + response.code();
                    if (response.code() == 404) {
                        errorMsg = "Postingan tidak ditemukan atau tidak diizinkan";
                    } else if (response.code() == 400) {
                        errorMsg = "Konten tidak valid";
                    }
                    Toast.makeText(EditPostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Error jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}