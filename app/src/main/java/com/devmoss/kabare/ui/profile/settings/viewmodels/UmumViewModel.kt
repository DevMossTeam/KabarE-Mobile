package com.devmoss.kabare.ui.profile.settings.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.model.User
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.model.UserResponse
import com.devmoss.kabare.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UmumViewModel : ViewModel() {

    private val _userData = MutableLiveData<Result<User>>()
    val userData: LiveData<Result<User>> get() = _userData

    fun fetchUserData(uid: String) {
        val request = UserRequest(uid)

        ApiConfig.getApiService().getUserData(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _userData.postValue(Result.success(response.body()!!.data))
                } else {
                    _userData.postValue(Result.failure(Throwable(response.message())))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _userData.postValue(Result.failure(t))
            }
        })
    }
}