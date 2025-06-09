package com.example.twist.api;

import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;
import com.example.twist.model.post.PostResponse;
import com.example.twist.model.post.PostPayload;
import com.example.twist.model.profile.ProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @GET("posts")
    Call<List<PostResponse>> getPosts(@Header("Authorization") String token);

    @POST("posts")
    Call<Void> createPost(@Header("Authorization") String token, @Body PostPayload postPayload);

    @GET("profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

    @GET("profile/posts")
    Call<List<PostResponse>> getUserPosts(@Header("Authorization") String token);
}