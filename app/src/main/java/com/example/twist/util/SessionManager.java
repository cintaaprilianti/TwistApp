package com.example.twist.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static final String PREF_NAME = "TwistPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String TAG = "SessionManager";

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "SessionManager initialized");
    }

    public void saveAuthData(String token, String username, int userId) {
        if (token != null && !token.isEmpty() && username != null && !username.isEmpty()) {
            prefs.edit()
                    .putString(KEY_TOKEN, token)
                    .putString(KEY_USERNAME, username)
                    .putInt(KEY_USER_ID, userId)
                    .apply();
            Log.d(TAG, "Auth data saved - Token: " + token.substring(0, 10) + "... Username: " + username + ", UserId: " + userId);
        } else {
            Log.w(TAG, "Attempted to save invalid auth data: token=" + token + ", username=" + username);
        }
    }

    public String getToken() {
        String token = prefs.getString(KEY_TOKEN, null);
        Log.d(TAG, "Token retrieved: " + (token != null ? token.substring(0, 10) + "..." : "null"));
        return token;
    }

    public String getUsername() {
        String username = prefs.getString(KEY_USERNAME, null);
        Log.d(TAG, "Username retrieved: " + (username != null ? username : "null"));
        return username;
    }

    public int getUserId() {
        int userId = prefs.getInt(KEY_USER_ID, -1);
        Log.d(TAG, "UserId retrieved: " + userId);
        return userId;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
        Log.d(TAG, "Session cleared");
    }
}