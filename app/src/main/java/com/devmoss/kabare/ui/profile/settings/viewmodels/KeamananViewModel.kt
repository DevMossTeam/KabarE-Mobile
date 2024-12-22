package com.devmoss.kabare.ui.profile.settings.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.data.model.ResponseCheckUser
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.model.User
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.model.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KeamananViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _emailUpdateStatus = MutableLiveData<Boolean>()
    val emailUpdateStatus: LiveData<Boolean> get() = _emailUpdateStatus

    private val _passwordChangeStatus = MutableLiveData<Boolean>()
    val passwordChangeStatus: LiveData<Boolean> get() = _passwordChangeStatus

    private val userRepository = UserRepository(application)

    private val _userData = MutableLiveData<Result<User>>()
    val userData: LiveData<Result<User>> get() = _userData

    fun fetchUserData(uid: String) {
        val request = UserRequest(uid = uid)

        ApiConfig.getApiService().getUserData(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        _userData.postValue(Result.success(responseBody.data))
                    } ?: run {
                        _userData.postValue(Result.failure(Throwable("Error: Response body is null")))
                    }
                } else {
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    _userData.postValue(Result.failure(Throwable(errorMessage)))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _userData.postValue(Result.failure(Throwable("Failed to fetch user data: ${t.message}")))
            }
        })
    }

    // Update email with availability check
    fun updateEmail(userUid: String, newEmail: String) {
        if (isValidEmail(newEmail)) {
            checkEmailAvailability(newEmail) { isAvailable, message ->
                if (isAvailable) {
                    // Proceed with updating the email
                    viewModelScope.launch {
                        val result = userRepository.updateEmail(userUid, newEmail)
                        _emailUpdateStatus.value = result.isSuccess
                        if (result.isSuccess) {
                            _email.value = newEmail
                        }
                    }
                } else {
                    // Show the error message that email is already taken
                    _emailUpdateStatus.value = false
                    // Handle error or display message (message contains the error message)
                }
            }
        } else {
            _emailUpdateStatus.value = false // Invalid email format
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Check if the email is already taken
    fun checkEmailAvailability(email: String, callback: (Boolean, String) -> Unit) {
        val emailCheckRequest = CheckUserRequest(email = email)
        ApiConfig.getApiService().checkUser(emailCheckRequest).enqueue(object : Callback<ResponseCheckUser> {
            override fun onResponse(call: Call<ResponseCheckUser>, response: Response<ResponseCheckUser>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        if (responseBody.exists) {
                            // Email exists
                            callback(false, "Email sudah terdaftar.")
                        } else {
                            // Email is available
                            callback(true, "")
                        }
                    } ?: run {
                        // If response body is null, assume email is available
                        callback(true, "")
                    }
                } else {
                    // Error in checking email
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    Log.e("EmailCheck", errorMessage)
                    callback(false, "Terjadi kesalahan saat memeriksa email.")
                }
            }

            override fun onFailure(call: Call<ResponseCheckUser>, t: Throwable) {
                // If failed to check email
                Log.e("EmailCheck", "Failed to check email availability: ${t.message}")
                callback(false, "Terjadi kesalahan saat memeriksa email.")
            }
        })
    }
}