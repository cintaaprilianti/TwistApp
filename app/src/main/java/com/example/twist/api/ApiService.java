package com.example.twist.api;

import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;
import com.example.twist.model.post.CommentResponse;
import com.example.twist.model.post.CreateCommentRequest;
import com.example.twist.model.post.PostResponse;
import com.example.twist.model.post.PostPayload;
import com.example.twist.model.profile.ProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @POST("posts/{postId}/like")
    Call<Void> likePost(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/repost")
    Call<Void> repostPost(@Header("Authorization") String token, @Path("postId") int postId);

    @GET("posts/{postId}/comments")
    Call<List<CommentResponse>> getPostComments(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/comments")
    Call<Void> addComment(@Header("Authorization") String token, @Path("postId") int postId, @Body CreateCommentRequest request);
}