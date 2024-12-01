package com.devmoss.kabare.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.utils.EmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val _signUpResult = MutableLiveData<AuthResult>()
    val signUpResult: LiveData<AuthResult> get() = _signUpResult

    private val _otpValidationResult = MutableLiveData<AuthResult>()
    val otpValidationResult: LiveData<AuthResult> get() = _otpValidationResult

    private val _otpExpiry = MutableLiveData<Long>()
    val otpExpiry: LiveData<Long> get() = _otpExpiry

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)

    fun register(email: String, username: String, fullName: String) {
        if (!validateInputs(email, username, fullName)) return

        _signUpResult.value = AuthResult(AuthStatus.LOADING, null)

        // Memeriksa apakah email atau username sudah ada di database
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService()  // Dapatkan instansi ApiInterface
                val emailCheckRequest = CheckUserRequest(email = email)
                val usernameCheckRequest = CheckUserRequest(username = username)

                // Memeriksa apakah email sudah ada
                val emailCheckResponse = apiService.checkUser(emailCheckRequest).execute()
                if (emailCheckResponse.isSuccessful && emailCheckResponse.body()?.exists == true) {
                    _signUpResult.postValue(
                        AuthResult(AuthStatus.FAILURE, "Email sudah terdaftar.")
                    )
                    return@launch
                }

                // Memeriksa apakah username sudah ada
                val usernameCheckResponse = apiService.checkUser(usernameCheckRequest).execute()
                if (usernameCheckResponse.isSuccessful && usernameCheckResponse.body()?.exists == true) {
                    _signUpResult.postValue(
                        AuthResult(AuthStatus.FAILURE, "Username sudah terdaftar.")
                    )
                    return@launch
                }

                // Jika email dan username belum terdaftar, lanjutkan ke proses pendaftaran
                saveUserDataToPreferences(fullName, username, email)
                val otp = generateOtp()
                saveOtpToPreferences(otp)
                sendOtp(email, otp)
            } catch (e: Exception) {
                _signUpResult.postValue(
                    AuthResult(AuthStatus.FAILURE, "Gagal mendaftar: ${e.localizedMessage}")
                )
            }
        }
    }

    private fun validateInputs(email: String, username: String, fullName: String): Boolean {
        return when {
            fullName.isBlank() || username.isBlank() || email.isBlank() -> {
                _signUpResult.value = AuthResult(AuthStatus.FAILURE, "Semua bidang wajib diisi.")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _signUpResult.value = AuthResult(AuthStatus.FAILURE, "Format email tidak valid.")
                false
            }
            else -> true
        }
    }

    private fun saveUserDataToPreferences(fullName: String, username: String, email: String) {
        sharedPreferences.edit().apply {
            putString("full_name", fullName)
            putString("username", username)
            putString("email", email)
            apply()
        }
    }

    private fun saveOtpToPreferences(otp: String) {
        val expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5) // 5 menit
        sharedPreferences.edit().apply {
            putString("otp", otp)
            putLong("otp_expiry", expiryTime)
            apply()
        }
        _otpExpiry.postValue(expiryTime)
    }

    private suspend fun sendOtp(email: String, otp: String) {
        EmailSender.sendOtpEmail(email, otp) { success, message ->
            if (success) {
                _signUpResult.postValue(
                    AuthResult(AuthStatus.SUCCESS, "OTP berhasil dikirim ke $email")
                )
            } else {
                _signUpResult.postValue(AuthResult(AuthStatus.FAILURE, message ?: "Kesalahan tidak diketahui."))
            }
        }
    }

    fun resendOtp() {
        val email = sharedPreferences.getString("email", null)
        if (email.isNullOrBlank()) {
            _signUpResult.value = AuthResult(AuthStatus.FAILURE, "Email tidak ditemukan.")
            return
        }

        _signUpResult.value = AuthResult(AuthStatus.LOADING, null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newOtp = generateOtp()
                saveOtpToPreferences(newOtp)
                sendOtp(email, newOtp)
            } catch (e: Exception) {
                _signUpResult.postValue(
                    AuthResult(AuthStatus.FAILURE, "Gagal mengirim ulang OTP: ${e.localizedMessage}")
                )
            }
        }
    }

    fun verifyOtp(enteredOtp: String) {
        val savedOtp = sharedPreferences.getString("otp", null)
        val expiryTime = sharedPreferences.getLong("otp_expiry", 0)

        if (System.currentTimeMillis() > expiryTime) {
            _otpValidationResult.value = AuthResult(AuthStatus.FAILURE, "OTP telah kedaluwarsa.")
            return
        }

        if (savedOtp == enteredOtp) {
            _otpValidationResult.value = AuthResult(AuthStatus.SUCCESS, "OTP valid.")
        } else {
            _otpValidationResult.value = AuthResult(AuthStatus.FAILURE, "OTP tidak valid.")
        }
    }


    private fun generateOtp(): String = (100000..999999).random().toString()

    data class AuthResult(val status: AuthStatus, val message: String?)

    enum class AuthStatus { SUCCESS, FAILURE, LOADING }
}
