package com.example.twist.activity.post;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.adapter.CommentAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private ApiService apiService;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        // Inisialisasi komponen
        recyclerView = findViewById(R.id.comment_recycler_view);
        commentAdapter = new CommentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        // Ambil postId dari intent
        postId = getIntent().getIntExtra("postId", -1);
        if (postId == -1) {
            Toast.makeText(this, "Post tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi ApiService
        apiService = ApiClient.getClient().create(ApiService.class);
        String token = getTokenFromStorage();
        if (token != null) {
            loadComments(token);
        } else {
            Toast.makeText(this, "Silakan login", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang komentar saat kembali ke aktivitas ini
        String token = getTokenFromStorage();
        if (token != null) {
            loadComments(token);
        }
    }

    private void loadComments(String token) {
        Call<List<CommentResponse>> call = apiService.getPostComments("Bearer " + token, postId);
        call.enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CommentListActivity", "Comments loaded: " + response.body().size());
                    commentAdapter.setCommentList(response.body());
                } else {
                    Log.e("CommentListActivity", "Error: " + response.code() + ", Body: " + (response.errorBody() != null ? response.errorBody().toString() : "No detail"));
                    Toast.makeText(CommentListActivity.this, "Gagal memuat komentar: Kode " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                Log.e("CommentListActivity", "Network error: " + t.getMessage());
                Toast.makeText(CommentListActivity.this, "Koneksi bermasalah: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getTokenFromStorage() {
        SharedPreferences prefs = getSharedPreferences("TwistPrefs", MODE_PRIVATE);
        return prefs.getString("auth_token", null);
    }
}