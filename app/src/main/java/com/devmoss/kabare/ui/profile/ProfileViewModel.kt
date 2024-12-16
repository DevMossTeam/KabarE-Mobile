package com.devmoss.kabare.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.model.User
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val userRepository = UserRepository(application)

    private val apiService = ApiConfig.getApiService()

    // Fetch user profile from the API
    fun fetchUserProfile() {
        val userId = userRepository.getUserUid()

        if (userId.isNullOrEmpty()) {
            _errorMessage.postValue("UID not found.")
            return
        }

        val userRequest = UserRequest(userId)

        apiService.getUserData(userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _userProfile.postValue(it.data)
                    } ?: run {
                        _errorMessage.postValue("User profile not found.")
                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _errorMessage.postValue("Network error: ${t.message}")
            }
        })
    }

    // Get login status from UserRepository
    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    // Clear error messages
    fun clearErrorMessage() {
        _errorMessage.postValue(null)
    }
}