package com.devmoss.kabare.ui.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.DeviceTokenRequest
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Call

class IntroViewModel : ViewModel() {

    private val _introStatus = MutableLiveData<IntroStatus>()
    val introStatus: LiveData<IntroStatus> get() = _introStatus

    private val _deviceToken = MutableLiveData<String>()
    val deviceToken: LiveData<String> get() = _deviceToken

    init {
        startIntro()
        fetchDeviceToken()
    }

    private fun startIntro() {
        _introStatus.value = IntroStatus.Loading
        viewModelScope.launch {
            _introStatus.value = IntroStatus.ShowIntro
            delay(100)
            proceedToNext()
        }
    }

    private fun proceedToNext() {
        _introStatus.value = IntroStatus.NavigateToWelcome
    }

    private fun fetchDeviceToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("DeviceToken", "FCM Token: $token")
                    _deviceToken.postValue(token)

                    // Kirim token ke server
                    sendDeviceTokenToServer(token)
                } else {
                    Log.e("DeviceToken", "Failed to get FCM token", task.exception)
                }
            }
    }

    private fun sendDeviceTokenToServer(token: String) {
        val request = DeviceTokenRequest(device_token = token)

        ApiConfig.getApiService().sendDeviceToken(request).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("DeviceToken", "Device token sent successfully: ${response.message()}")
                } else {
                    Log.e("DeviceToken", "Failed to send device token: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DeviceToken", "Error sending device token", t)
            }
        })
    }

    sealed class IntroStatus {
        object Loading : IntroStatus()
        object ShowIntro : IntroStatus()
        object NavigateToWelcome : IntroStatus()
    }
}