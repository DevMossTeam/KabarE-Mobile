package com.devmoss.kabare.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.utils.EmailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LupaPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val _emailVerificationResult = MutableLiveData<AuthResult>()
    val emailVerificationResult: LiveData<AuthResult> get() = _emailVerificationResult

    private val _otpValidationResult = MutableLiveData<AuthResult>()
    val otpValidationResult: LiveData<AuthResult> get() = _otpValidationResult

    private val _passwordResetResult = MutableLiveData<AuthResult>()
    val passwordResetResult: LiveData<AuthResult> get() = _passwordResetResult

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("LupaPasswordPrefs", Context.MODE_PRIVATE)

    private val otpExpiryTimeMinutes = 5

    fun resendOtp() {
        val email = sharedPreferences.getString("email", null)
        if (email.isNullOrBlank()) {
            _emailVerificationResult.value =
                AuthResult(AuthStatus.FAILURE, "Email tidak ditemukan.")
            return
        }

        _emailVerificationResult.value = AuthResult(AuthStatus.LOADING, null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newOtp = generateOtp()
                saveOtpToPreferences(newOtp)
                sendOtp(email, newOtp)
            } catch (e: Exception) {
                _emailVerificationResult.postValue(
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

    private fun saveOtpToPreferences(otp: String) {
        val expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(otpExpiryTimeMinutes.toLong())
        sharedPreferences.edit().apply {
            putString("otp", otp)
            putLong("otp_expiry", expiryTime)
            apply()
        }
    }

    private suspend fun sendOtp(email: String, otp: String) {
        EmailSender.sendOtpEmail(email, otp) { success, message ->
            if (success) {
                _emailVerificationResult.postValue(
                    AuthResult(AuthStatus.SUCCESS, "OTP berhasil dikirim ke $email.")
                )
            } else {
                _emailVerificationResult.postValue(
                    AuthResult(AuthStatus.FAILURE, message ?: "Kesalahan tidak diketahui.")
                )
            }
        }
    }

    private fun generateOtp(): String = (100000..999999).random().toString()

    data class AuthResult(val status: AuthStatus, val message: String?)

    enum class AuthStatus { SUCCESS, FAILURE, LOADING }
}