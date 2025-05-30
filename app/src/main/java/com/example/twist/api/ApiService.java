package com.example.twist.api;

import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}
