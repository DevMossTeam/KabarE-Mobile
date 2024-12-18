package com.devmoss.kabare.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.model.UserUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {

    // SharedPreferences for storing user data
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Save login status
    fun saveLoginStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", isLoggedIn)
            .apply()
        Log.d(TAG, "Login status saved: $isLoggedIn")
    }

    // Get login status
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // Save user UID and set login status
    fun saveUserUid(uid: String?) {
        sharedPreferences.edit()
            .putString("uid", uid)
            .apply()

        val isLoggedIn = !uid.isNullOrEmpty()
        saveLoginStatus(isLoggedIn)
        Log.d(TAG, "User UID saved: $uid, Login status: $isLoggedIn")
    }

    // Get user UID
    fun getUserUid(): String? {
        return sharedPreferences.getString("uid", null)
    }

    // Clear all user data
    fun clearUserData() {
        sharedPreferences.edit()
            .clear()
            .apply()
        saveLoginStatus(false) // After clearing data, set user as logged out
        Log.d(TAG, "All user data cleared from SharedPreferences")
    }

    // Update user data via API
    suspend fun updateUser(
        userUid: String,
        namaLengkap: String?,
        username: String?,
        profilePic: String?
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Ensure non-null values for UserRequest
                val userUpdateRequest = UserUpdateRequest(
                    uid = userUid, // Should always be non-null
                    nama_lengkap = namaLengkap ?: "", // Fallback to empty string if null
                    nama_pengguna = username ?: "",   // Fallback to empty string if null
                    profile_pic = profilePic ?: ""    // Fallback to empty string if null
                )

                // Call API to update user
                val response = ApiConfig.getApiService().updateUserData(userUpdateRequest).execute()

                if (response.isSuccessful) {
                    Log.d(TAG, "User data updated successfully: ${response.body()}")
                    Result.success(Unit)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update user data: $errorMessage")
                    Result.failure(Exception("Failed to update user data: ${response.code()} - $errorMessage"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user data", e)
                Result.failure(e)
            }
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}