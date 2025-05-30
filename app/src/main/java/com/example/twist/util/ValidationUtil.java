package com.example.twist.util;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtil {
    public static boolean isValidUsername(String username) {
        return !TextUtils.isEmpty(username);
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }

    public static boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}