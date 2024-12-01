package com.devmoss.kabare.ui.auth.popups

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.lifecycle.lifecycleScope
import com.devmoss.kabare.R
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.utils.EmailSender
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LupaPasswordKonfirmasiEmailDialog : BottomSheetDialogFragment() {

    private lateinit var btnKirim: Button
    private lateinit var btnCancel: TextView
    private lateinit var emailEditText: TextInputEditText
    private lateinit var progressBar: View
    private var isProcessing = false

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_lupa_password_konfirmasi_email, container, false)

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("LupaPasswordPrefs", Context.MODE_PRIVATE)

        // Initialize views
        btnKirim = view.findViewById(R.id.btn_kirim)
        btnCancel = view.findViewById(R.id.tv_cancel)
        emailEditText = view.findViewById(R.id.et_email)
        progressBar = view.findViewById(R.id.progressBar)

        btnKirim.setOnClickListener { handleKirimClick() }
        btnCancel.setOnClickListener { if (!isProcessing) dismiss() }

        // Automatically open the keyboard for email input
        view.post {
            emailEditText.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(emailEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        return view
    }

    private fun handleKirimClick() {
        if (isProcessing) return

        val email = emailEditText.text.toString().trim()
        if (!validateEmail(email)) {
            emailEditText.error = "Masukkan alamat email yang valid"
            return
        }

        isProcessing = true
        showLoading(true)
        checkEmailAndSendOtp(email)
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkEmailAndSendOtp(email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService()
                val checkUserRequest = CheckUserRequest(email = email)
                val response = apiService.checkUser(checkUserRequest).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.exists == true) {
                        val otp = generateOtp()
                        saveOtpToPreferences(email, otp)
                        sendOtp(email, otp)
                        openVerificationDialog(email)
                    } else {
                        emailEditText.error = "Email tidak ditemukan"
                        resetState()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Kesalahan: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    resetState()
                }
            }
        }
    }

    private suspend fun sendOtp(email: String, otp: String) {
        Log.d("OTP_SENT", "Sending OTP: $otp to email: $email") // Log the sent OTP
        EmailSender.sendOtpEmail(email, otp) { success, message ->
            lifecycleScope.launch(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        "OTP berhasil dikirim ke $email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        message ?: "Kesalahan saat mengirim OTP.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                resetState()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnKirim.isEnabled = !isLoading
        btnCancel.isEnabled = !isLoading
        emailEditText.isEnabled = !isLoading
    }

    private fun resetState() {
        isProcessing = false
        showLoading(false)
    }

    private fun generateOtp(): String = (100000..999999).random().toString()

    private fun saveOtpToPreferences(email: String, otp: String) {
        val expiryTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5) // OTP valid for 5 minutes
        sharedPreferences.edit().apply {
            putString("otp", otp)
            putLong("otp_expiry", expiryTime)
            putString("email", email) // Save email for later use
            apply()
        }
    }

    private fun openVerificationDialog(email: String) {
        resetState()
        dismiss() // Close the current dialog

        val verifEmailDialog = LupaPasswordVerifEmailDialog().apply {
            setEmail(email) // Pass email to verification dialog
        }
        verifEmailDialog.show(parentFragmentManager, "LupaPasswordVerifEmailDialog")
    }
}