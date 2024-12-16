package com.devmoss.kabare.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class UserRepository(context: Context) {

    // SharedPreferences to store user data
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Save login status
    fun saveLoginStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", isLoggedIn)
            .apply()
        Log.d("UserRepository", "Login status saved: $isLoggedIn")
    }

    // Get login status
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // Save user UID and set login status to true
    fun saveUserUid(uid: String?) {
        if (uid == null) {
            Log.d("UserRepository", "User UID is null")
            saveLoginStatus(false)  // If UID is null, user is not logged in
        } else {
            sharedPreferences.edit()
                .putString("uid", uid)
                .apply()
            saveLoginStatus(true)  // If UID is not null, user is logged in
            Log.d("UserRepository", "User UID saved: $uid")
        }
    }

    // Get user UID
    fun getUserUid(): String? {
        return sharedPreferences.getString("uid", null) // Return UID if exists, else null
    }

    // Clear all user data and set login status to false
    fun clearUserData() {
        sharedPreferences.edit()
            .clear()
            .apply()
        saveLoginStatus(false)  // After clearing data, the user is logged out
        Log.d("UserRepository", "All user data cleared from SharedPreferences")
    }
}