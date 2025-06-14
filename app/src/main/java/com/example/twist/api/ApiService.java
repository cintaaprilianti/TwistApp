package com.example.twist.api;

import com.example.twist.model.auth.AuthRequest;
import com.example.twist.model.auth.AuthResponse;
import com.example.twist.model.post.CommentResponse;
import com.example.twist.model.post.CreateCommentRequest;
import com.example.twist.model.post.PostResponse;
import com.example.twist.model.post.PostPayload;
import com.example.twist.model.profile.ProfilePostsResponse;
import com.example.twist.model.profile.ProfileResponse;
import com.example.twist.model.profile.UpdateProfileRequest;
import com.example.twist.model.search.SearchUserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // auth //
    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/forgot-password")
    Call<AuthResponse> forgotPassword(@Body AuthRequest request);

    @POST("auth/reset-password")
    Call<AuthResponse> resetPassword(@Body AuthRequest request);

    // posts //
    @GET("posts")
    Call<List<PostResponse>> getPosts(@Header("Authorization") String token);

    @POST("posts")
    Call<Void> createPost(@Header("Authorization") String token, @Body PostPayload postPayload);

    @PUT("posts/{postId}")
    Call<PostResponse> updatePost(@Header("Authorization") String token, @Path("postId") int postId, @Body PostPayload payload);

    @POST("posts/{postId}/like")
    Call<Void> likePost(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/repost")
    Call<Void> repostPost(@Header("Authorization") String token, @Path("postId") int postId);

    @DELETE("posts/{postId}")
    Call<Void> deletePost(@Header("Authorization") String token, @Path("postId") int postId);

    @GET("posts/{postId}/comments")
    Call<List<CommentResponse>> getPostComments(@Header("Authorization") String token, @Path("postId") int postId);

    @POST("posts/{postId}/comments")
    Call<Void> addComment(@Header("Authorization") String token, @Path("postId") int postId, @Body CreateCommentRequest request);

    // profile //
    @GET("profile/{username}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String authHeader, @Path("username") String username);

    @GET("profile/{username}/posts")
    Call<ProfilePostsResponse> getProfilePosts(
            @Header("Authorization") String token,
            @Path("username") String username,
            @Query("tab") String tab,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    // users //
    @PATCH("users/update")
    Call<ProfileResponse> updateProfile(@Header("Authorization") String authHeader, @Body UpdateProfileRequest request);

    @DELETE("users/delete")
    Call<Void> deleteAccount(@Header("Authorization") String token);

    @POST("users/{userId}/follow")
    Call<Void> followUser(@Header("Authorization") String authToken, @Path("userId") String userId);

    @DELETE("users/{userId}/follow")
    Call<Void> unfollowUser(@Header("Authorization") String authToken, @Path("userId") String userId);

    @GET("users/{userId}/posts")
    Call<List<PostResponse>> getUserPosts(@Header("Authorization") String authHeader, @Path("userId") String userId);

    // search //
    @GET("search")
    Call<SearchUserResponse> searchUsers(
            @Header("Authorization") String token,
            @Query("q") String query,
            @Query("type") String type
    );
}