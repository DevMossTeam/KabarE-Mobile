package com.devmoss.kabare.ui.profile.settings.viewmodels

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

class PengaturanAkunViewModel(application: Application) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> get() = _userRole

    private val _imgProfile = MutableLiveData<String?>()
    val imgProfile: LiveData<String?> get() = _imgProfile

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    private val userRepository: UserRepository = UserRepository(application) // UserRepository instance
    private val apiService = ApiConfig.getApiService()

    // Fetch user data from API
    fun fetchUserData() {
        val userId = userRepository.getUserUid()
        if (userId.isNullOrEmpty()) {
            _userName.postValue("Guest")
            _userRole.postValue("Guest")
            return
        }

        val userRequest = UserRequest(userId)
        apiService.getUserData(userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { user ->
                        _userName.postValue(user.nama_pengguna)
                        _userRole.postValue(user.role)
                        _imgProfile.postValue(user.profile_pic)
                    }
                } else {
                    _userName.postValue("Error")
                    _userRole.postValue("Error")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _userName.postValue("Error")
                _userRole.postValue("Error")
            }
        })
    }
    fun logout() {
        userRepository.clearUserData() // Clears user data from shared preferences
        _logoutStatus.value = true // Update LiveData to notify the UI
    }
}