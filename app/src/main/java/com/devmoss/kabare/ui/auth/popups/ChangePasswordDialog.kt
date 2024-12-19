package com.devmoss.kabare.ui.auth.popups

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.devmoss.kabare.R
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.model.PasswordChangeRequest
import com.devmoss.kabare.model.UserResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordDialog(private val userRepository: UserRepository) : BottomSheetDialogFragment() {

    private lateinit var btnChangePassword: Button
    private lateinit var btnCancel: TextView
    private lateinit var currentPasswordEditText: TextInputEditText
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_change_password, container, false)

        // Initialize views
        btnChangePassword = view.findViewById(R.id.btn_change_password)
        btnCancel = view.findViewById(R.id.tv_cancel)
        currentPasswordEditText = view.findViewById(R.id.et_current_password)
        newPasswordEditText = view.findViewById(R.id.et_new_password)
        confirmPasswordEditText = view.findViewById(R.id.et_confirm_password)

        // Set up listeners
        btnChangePassword.setOnClickListener { handlePasswordChange() }
        btnCancel.setOnClickListener { dismiss() }

        // Show keyboard when dialog opens
        view.post {
            currentPasswordEditText.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(currentPasswordEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        return view
    }

    private fun handlePasswordChange() {
        val uid = userRepository.getUserUid()
        if (uid.isNullOrEmpty()) {
            Toast.makeText(context, "User UID tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }

        val currentPassword = currentPasswordEditText.text.toString().trim()
        val newPassword = newPasswordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Validate inputs
        when {
            currentPassword.isEmpty() -> currentPasswordEditText.error = "Password saat ini tidak boleh kosong"
            newPassword.isEmpty() -> newPasswordEditText.error = "Password baru tidak boleh kosong"
            confirmPassword.isEmpty() -> confirmPasswordEditText.error = "Konfirmasi password tidak boleh kosong"
            newPassword != confirmPassword -> confirmPasswordEditText.error = "Password tidak cocok"
            else -> performPasswordChange(uid, currentPassword, newPassword)
        }
    }

    private fun performPasswordChange(uid: String, currentPassword: String, newPassword: String) {
        val request = PasswordChangeRequest(uid = uid, current_password = currentPassword, new_password = newPassword)

        ApiConfig.getApiService().changePassword(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Password berhasil diganti", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Terjadi kesalahan saat mengganti password"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, "Terjadi kesalahan: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}