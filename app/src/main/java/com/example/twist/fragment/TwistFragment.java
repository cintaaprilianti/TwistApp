package com.example.twist.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twist.R;
import com.example.twist.adapter.TwistAdapter;
import com.example.twist.api.ApiClient;
import com.example.twist.api.ApiService;
import com.example.twist.model.post.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwistFragment extends Fragment {

    private RecyclerView recyclerView;
    private TwistAdapter twistAdapter;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twist_fragment, container, false);

        recyclerView = view.findViewById(R.id.twistRecyclerView);
        twistAdapter = new TwistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(twistAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        loadTwists();

        return view;
    }

    private void loadTwists() {
        String token = requireActivity().getSharedPreferences("TwistPrefs", MODE_PRIVATE).getString("auth_token", null);
        if (token == null) {
            Toast.makeText(getContext(), "Silakan login", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getUserPosts("Bearer " + token).enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    twistAdapter.setTwistList(response.body());
                } else {
                    Toast.makeText(getContext(), "Gagal memuat twist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}