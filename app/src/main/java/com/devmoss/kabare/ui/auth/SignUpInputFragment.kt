package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R
import com.google.android.material.textfield.TextInputLayout

class SignUpInputFragment : Fragment() {

    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSavePassword: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        newPasswordInputLayout = view.findViewById(R.id.new_password_input_layout)
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_input_layout)
        etNewPassword = view.findViewById(R.id.et_new_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)
        btnSavePassword = view.findViewById(R.id.btn_save_password)

        // Set up the button click listener
        btnSavePassword.setOnClickListener {
            onSavePasswordClicked()
        }

        // Optional: Add a TextWatcher to the EditText for input validation
        etNewPassword.doOnTextChanged { text, _, _, _ ->
            if (!isPasswordValid(text.toString())) {
                newPasswordInputLayout.error = "Password must be at least 6 characters"
            } else {
                newPasswordInputLayout.error = null
            }
        }

        etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != etNewPassword.text.toString()) {
                confirmPasswordInputLayout.error = "Passwords do not match"
            } else {
                confirmPasswordInputLayout.error = null
            }
        }
    }

    private fun onSavePasswordClicked() {
        val newPassword = etNewPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (isFormValid(newPassword, confirmPassword)) {
            // Save password logic here
            Toast.makeText(requireContext(), "Password saved successfully!", Toast.LENGTH_SHORT).show()
            // Navigate to the next fragment or activity here
        }
    }

    private fun isFormValid(newPassword: String, confirmPassword: String): Boolean {
        return when {
            !isPasswordValid(newPassword) -> {
                newPasswordInputLayout.error = "Password must be at least 6 characters"
                false
            }
            confirmPassword.isEmpty() -> {
                confirmPasswordInputLayout.error = "Please confirm your password"
                false
            }
            newPassword != confirmPassword -> {
                confirmPasswordInputLayout.error = "Passwords do not match"
                false
            }
            else -> {
                newPasswordInputLayout.error = null
                confirmPasswordInputLayout.error = null
                true
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}