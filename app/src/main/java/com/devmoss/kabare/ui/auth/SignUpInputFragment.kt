package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.addCallback
import com.devmoss.kabare.ui.auth.viewmodels.SignUpInputViewModel

class SignUpInputFragment : Fragment(R.layout.fragment_sign_up_input) {

    private val viewModel: SignUpInputViewModel by activityViewModels()

    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSavePassword: Button
    private lateinit var progressBar: ProgressBar

    private var doubleBackToExitPressedOnce = false // Flag untuk mendeteksi back-to-back
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        newPasswordInputLayout = view.findViewById(R.id.new_password_input_layout)
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_input_layout)
        etNewPassword = view.findViewById(R.id.et_new_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)
        btnSavePassword = view.findViewById(R.id.btn_save_password)
        progressBar = view.findViewById(R.id.progressBar)

        // Set up button click listener
        btnSavePassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (isFormValid(newPassword, confirmPassword)) {
                showLoading(true) // Show the loading indicator
                viewModel.savePassword(newPassword, confirmPassword)
            }
        }

        // Observe password registration result
        viewModel.firebaseRegistrationResult.observe(viewLifecycleOwner) { result ->
            showLoading(false) // Hide the loading indicator once registration is complete

            Log.d("SignUpInputFragment", "Registration result: $result")

            if (result == "Registration successful!") {
                Log.d("SignUpInputFragment", "Navigating to IntroFragment...")

                if (isAdded) { // Check if fragment is attached before navigating
                    findNavController().navigate(R.id.action_signUpInputFragment_to_introFragment)
                } else {
                    Log.e("SignUpInputFragment", "Fragment is not added to the activity")
                }
            } else {
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }

        // Add TextWatcher for password validation
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

        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                // Exit the app
                requireActivity().finish()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(requireContext(), "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

                // Reset flag after 2 seconds
                handler.postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            btnSavePassword.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            btnSavePassword.isEnabled = true
        }
    }
}