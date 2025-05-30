package com.example.twist.network;

import com.example.twist.model.request.LoginRequest;
import com.example.twist.model.request.RegisterRequest;
import com.example.twist.model.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST(ApiEndpoints.LOGIN)
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST(ApiEndpoints.REGISTER)
    Call<AuthResponse> register(@Body RegisterRequest request);
}
