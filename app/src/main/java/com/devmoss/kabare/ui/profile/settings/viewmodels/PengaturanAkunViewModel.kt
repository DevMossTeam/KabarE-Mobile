package com.devmoss.kabare.ui.profile.settings.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaturanAkunViewModel(application: Application) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> get() = _userRole

    private val _imgProfile = MutableLiveData<String?>()
    val imgProfile: LiveData<String?> get() = _imgProfile

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val userRepository: UserRepository = UserRepository(application)
    private val apiService = ApiConfig.getApiService()

    // Fetch user data
    fun fetchUserData() {
        val userId = userRepository.getUserUid()
        if (userId.isNullOrEmpty()) {
            handleGuestUser()
            return
        }

        val userRequest = UserRequest(userId)
        apiService.getUserData(userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { user ->
                        updateUserData(user.nama_pengguna, user.role, user.profile_pic)
                        _isUserLoggedIn.postValue(true)
                    } ?: handleErrorState()
                } else {
                    handleErrorState()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                handleErrorState()
            }
        })
    }

    private fun handleGuestUser() {
        updateUserData("Guest", "Guest", null)
        _isUserLoggedIn.postValue(false)
    }

    private fun updateUserData(name: String, role: String, profilePic: String?) {
        _userName.postValue(name)
        _userRole.postValue(role)
        _imgProfile.postValue(profilePic)
    }

    private fun handleErrorState() {
        updateUserData("Error", "Error", null)
        _isUserLoggedIn.postValue(false)
    }

    // Logout function
    fun logout() {
        userRepository.clearUserData()
        _logoutStatus.postValue(true)
        _isUserLoggedIn.postValue(false)
    }

    // Check if user is logged in (used during initialization)
    fun checkUserLoginStatus() {
        _isUserLoggedIn.postValue(userRepository.isUserLoggedIn())
    }
}