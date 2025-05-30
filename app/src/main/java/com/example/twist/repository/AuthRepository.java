package com.example.twist.repository;

import com.example.twist.model.request.LoginRequest;
import com.example.twist.model.request.RegisterRequest;
import com.example.twist.model.response.AuthResponse;
import com.example.twist.network.ApiClient;
import com.example.twist.network.AuthService;

import retrofit2.Call;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository() {
        authService = ApiClient.getClient().create(AuthService.class);
    }

    public Call<AuthResponse> login(LoginRequest request) {
        return authService.login(request);
    }

    public Call<AuthResponse> register(RegisterRequest request) {
        return authService.register(request);
    }
}
