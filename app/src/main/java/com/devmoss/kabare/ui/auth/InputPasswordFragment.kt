package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class InputPasswordFragment : Fragment() {

    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var saveButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_password, container, false)

        // Initialize UI components
        initializeUIComponents(view)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up save button click listener
        saveButton.setOnClickListener {
            updatePassword()
        }

        return view
    }

    private fun initializeUIComponents(view: View) {
        newPasswordInputLayout = view.findViewById(R.id.new_password_input_layout)
        newPasswordEditText = view.findViewById(R.id.et_new_password)
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_input_layout)
        confirmPasswordEditText = view.findViewById(R.id.et_confirm_password)
        saveButton = view.findViewById(R.id.btn_save_password)
    }

    private fun updatePassword() {
        val newPassword = newPasswordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (validateInputs(newPassword, confirmPassword)) {
            auth.currentUser?.updatePassword(newPassword)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                        // Redirect user to login or another fragment
                    } else {
                        Toast.makeText(requireContext(), "Password update failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun validateInputs(newPassword: String, confirmPassword: String): Boolean {
        var isValid = true

        if (newPassword.isEmpty()) {
            newPasswordInputLayout.error = "Password cannot be empty"
            isValid = false
        } else {
            newPasswordInputLayout.error = null
        }

        if (confirmPassword.isEmpty()) {

            confirmPasswordInputLayout.error = "Confirm your password"
            isValid = false
        } else {
            confirmPasswordInputLayout.error = null
        }

        if (newPassword != confirmPassword) {
            confirmPasswordInputLayout.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }
}