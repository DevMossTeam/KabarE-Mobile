package com.devmoss.kabare.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResetPasswordRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordViewModel(application: Application) : AndroidViewModel(application) {

    // Sealed class to represent the various states
    sealed class ResetPasswordState {
        object Success : ResetPasswordState()
        data class Error(val message: String) : ResetPasswordState()
        data class ValidationError(
            val newPasswordError: String? = null,
            val confirmPasswordError: String? = null
        ) : ResetPasswordState()
    }

    private val _resetPasswordState = MutableLiveData<ResetPasswordState>()
    val resetPasswordState: LiveData<ResetPasswordState> get() = _resetPasswordState

    // Fetch the email stored in SharedPreferences
    private fun getEmailFromPreferences(): String? {
        val sharedPreferences = getApplication<Application>()
            .getSharedPreferences("LupaPasswordPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }

    fun resetPassword(newPassword: String, confirmPassword: String) {
        // Validate user inputs
        if (!validateInputs(newPassword, confirmPassword)) return

        // Get the email or show an error if it's missing
        val email = getEmailFromPreferences()
        if (email.isNullOrEmpty()) {
            _resetPasswordState.postValue(ResetPasswordState.Error("Email not found. Please try again."))
            return
        }

        // Log the email and password
        Log.d("ResetPasswordViewModel", "Email: $email")
        Log.d("ResetPasswordViewModel", "New Password: $newPassword")

        // Make the API call
        val apiService = ApiConfig.getApiService()
        val request = ResetPasswordRequest(email, newPassword)

        apiService.resetPassword(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _resetPasswordState.postValue(ResetPasswordState.Success)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    _resetPasswordState.postValue(ResetPasswordState.Error(errorMessage))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _resetPasswordState.postValue(
                    ResetPasswordState.Error("Failed to reset password: ${t.localizedMessage}")
                )
            }
        })
    }

    private fun validateInputs(newPassword: String, confirmPassword: String): Boolean {
        var isValid = true
        var newPasswordError: String? = null
        var confirmPasswordError: String? = null

        // Validate the new password
        if (newPassword.isBlank()) {
            newPasswordError = "Password cannot be empty"
            isValid = false
        } else if (newPassword.length < 6) {
            newPasswordError = "Password must be at least 6 characters"
            isValid = false
        }

        // Validate the confirmation password
        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Confirm password cannot be empty"
            isValid = false
        } else if (newPassword != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        // Post validation errors
        if (!isValid) {
            _resetPasswordState.value = ResetPasswordState.ValidationError(
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return isValid
    }
}