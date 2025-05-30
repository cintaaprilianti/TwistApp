package com.example.twist.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String token, int userId, String username, String email) {
        editor.putString(Constants.KEY_TOKEN, token);
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USERNAME, username);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getToken() { return sharedPreferences.getString(Constants.KEY_TOKEN, ""); }
    public int getUserId() { return sharedPreferences.getInt(Constants.KEY_USER_ID, -1); }
    public String getUsername() { return sharedPreferences.getString(Constants.KEY_USERNAME, ""); }
    public String getEmail() { return sharedPreferences.getString(Constants.KEY_EMAIL, ""); }
    public boolean isLoggedIn() { return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false); }
    public void clearSession() { editor.clear().apply(); }
}
