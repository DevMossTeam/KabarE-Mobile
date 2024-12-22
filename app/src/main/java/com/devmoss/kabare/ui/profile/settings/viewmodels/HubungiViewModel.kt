// HubungiViewModel.kt
package com.devmoss.kabare.ui.profile.settings.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class HubungiViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        _isUserLoggedIn.postValue(userRepository.isUserLoggedIn())
    }

    fun submitComplaint(message: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Retrieve user ID from UserRepository
        val userId = userRepository.getUserUid()

        // Check if the message is not empty before making the API call
        if (message.isEmpty()) {
            onFailure("Harap masukkan keluhan anda")
            return
        }

        // Prepare the request body, only including user_id if it exists
        val complaintData = mutableMapOf<String, String>("message" to message)

        // Add user_id to the request only if it is not null
        userId?.let {
            complaintData["user_id"] = it
        }

        // Use viewModelScope to launch the API call in the background
        viewModelScope.launch {
            try {
                // Make the API call with the complaint data
                val response: Response<ResponseBody> = ApiConfig.getApiService()
                    .submitComplaint(complaintData)

                if (response.isSuccessful) {
                    // Success callback
                    onSuccess()
                } else {
                    // Failure callback (backend failure)
                    onFailure("Terjadi kesalahan saat mengirim keluhan")
                }
            } catch (e: Exception) {
                // Handle any exception (e.g., network failure)
                onFailure("Terjadi kesalahan saat mengirim keluhan: ${e.message}")
            }
        }
    }
}