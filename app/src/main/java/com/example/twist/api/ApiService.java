package com.example.twist.api;

import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/forgot-password")
    Call<AuthResponse> forgotPassword(@Body AuthRequest request);

    @POST("auth/reset-password")
    Call<AuthResponse> resetPassword(@Body AuthRequest request);
}