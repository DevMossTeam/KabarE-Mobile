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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordDialog : BottomSheetDialogFragment() {

    private lateinit var btnChangePassword: Button
    private lateinit var btnCancel: TextView
    private lateinit var currentPasswordEditText: TextInputEditText
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this bottom sheet
        val view = inflater.inflate(R.layout.dialog_change_password, container, false)

        // Initialize views
        btnChangePassword = view.findViewById(R.id.btn_change_password)
        btnCancel = view.findViewById(R.id.tv_cancel)
        currentPasswordEditText = view.findViewById(R.id.et_current_password)
        newPasswordEditText = view.findViewById(R.id.et_new_password)
        confirmPasswordEditText = view.findViewById(R.id.et_confirm_password)

        // Set up change password button click listener
        btnChangePassword.setOnClickListener {
            changePassword()
        }

        // Set up cancel button click listener
        btnCancel.setOnClickListener {
            dismiss() // Dismiss the dialog
        }

        // Show keyboard automatically when dialog is opened
        view.post {
            currentPasswordEditText.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(currentPasswordEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        return view
    }

    // Validate inputs and perform password change
    private fun changePassword() {
        val currentPassword = currentPasswordEditText.text.toString().trim()
        val newPassword = newPasswordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (currentPassword.isEmpty()) {
            currentPasswordEditText.error = "Password saat ini tidak boleh kosong"
            return
        }
        if (newPassword.isEmpty()) {
            newPasswordEditText.error = "Password baru tidak boleh kosong"
            return
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Konfirmasi password tidak boleh kosong"
            return
        }
        if (newPassword != confirmPassword) {
            confirmPasswordEditText.error = "Password tidak cocok"
            return
        }

        // Perform password change logic here
        // You can call an API or perform local password update

        Toast.makeText(context, "Password berhasil diganti", Toast.LENGTH_SHORT).show()
        dismiss()
    }
}