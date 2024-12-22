package com.devmoss.kabare.ui.profile.settings.viewmodels

import android.app.Application
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import com.devmoss.kabare.model.User
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.model.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.data.model.ResponseCheckUser

class UmumViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)

    private val _userData = MutableLiveData<Result<User>>()
    val userData: LiveData<Result<User>> get() = _userData

    private val _updateResult = MutableLiveData<Result<Unit>>()

    // Fungsi untuk mengambil data pengguna
    fun fetchUserData(uid: String) {
        val request = UserRequest(uid = uid)

        ApiConfig.getApiService().getUserData(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _userData.postValue(Result.success(it.data))
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

    // Fungsi untuk mengecek ketersediaan username
    fun checkUsernameAvailability(username: String, callback: (Boolean) -> Unit) {
        val usernameCheckRequest = CheckUserRequest(username = username)

        // Make the API request asynchronously using enqueue
        ApiConfig.getApiService().checkUser(usernameCheckRequest).enqueue(object : Callback<ResponseCheckUser> {

            override fun onResponse(call: Call<ResponseCheckUser>, response: Response<ResponseCheckUser>) {
                if (response.isSuccessful) {
                    // Check if the response body is valid
                    response.body()?.let { responseBody ->
                        // Check if the username already exists
                        if (responseBody.exists) {
                            // Username is already taken
                            callback(false)
                        } else {
                            // Username is available
                            callback(true)
                        }
                    } ?: run {
                        // If response body is null, assume username is available
                        callback(true)
                    }
                } else {
                    // If there is an error in the response, assume username is available
                    val errorMessage = "Error: ${response.code()} ${response.message()}"
                    Log.e("UsernameCheck", errorMessage)
                    callback(true)
                }
            }

            override fun onFailure(call: Call<ResponseCheckUser>, t: Throwable) {
                // If the request fails, assume username is available
                Log.e("UsernameCheck", "Failed to check username availability: ${t.message}")
                callback(true)
            }
        })
    }

    // Fungsi untuk memperbarui data pengguna
    fun updateUserData(
        userUid: String,
        namaLengkap: String,
        username: String,
        profilePic: String? = null // Optional parameter for profile picture
    ) {
        // Pengecekan ketersediaan username
        checkUsernameAvailability(username) { isUsernameAvailable ->
            if (isUsernameAvailable) {
                // Indicate loading state or clear previous errors if needed
                _updateResult.postValue(Result.success(Unit)) // Resetting previous result state

                viewModelScope.launch {
                    try {
                        // Call the repository to update the user data, including profilePic if provided
                        val result = userRepository.updateUser(
                            userUid = userUid,
                            namaLengkap = namaLengkap,
                            username = username,
                            profilePic = profilePic ?: "" // Use empty string if profilePic is null
                        )

                        if (result.isSuccess) {
                            // Fetch the latest user data after successful update
                            fetchUserData(userUid)

                            // Post the successful result
                            _updateResult.postValue(Result.success(Unit))
                        } else {
                            // Post failure if the result is unsuccessful
                            _updateResult.postValue(Result.failure(result.exceptionOrNull() ?: Exception("Unknown error")))
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error updating user data", e)
                        _updateResult.postValue(Result.failure(e))
                    }
                }
            } else {
                // Post error result if the username is already taken
                _updateResult.postValue(Result.failure(Exception("Username sudah terdaftar.")))
            }
        }
    }
}