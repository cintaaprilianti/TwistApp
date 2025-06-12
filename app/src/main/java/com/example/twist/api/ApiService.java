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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("profile/{username}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token, @Path("username") String username);

    @GET("profile/{username}/posts")
    Call<ProfileResponse> getProfilePosts(
            @Header("Authorization") String token,
            @Path("username") String username,
            @Query("tab") String tab,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @PUT("profile/{username}")
    Call<ProfileResponse> updateProfile(@Header("Authorization") String token, @Path("username") String username, @Body ProfileResponse profile);

    @POST("posts/{postId}/like")
    Call<Void> likePost(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/repost")
    Call<Void> repostPost(@Header("Authorization") String token, @Path("postId") int postId);

    @GET("posts/{postId}/comments")
    Call<List<CommentResponse>> getPostComments(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/comments")
    Call<Void> addComment(@Header("Authorization") String token, @Path("postId") int postId, @Body CreateCommentRequest request);
}