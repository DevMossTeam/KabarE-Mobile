package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.popups.SignUpVerifikasiEmailDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment() {

    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var confirmButton: Button
    private lateinit var alreadyHaveAccountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        // Initialize UI components
        fullNameInputLayout = view.findViewById(R.id.full_name_input_layout)
        fullNameEditText = view.findViewById(R.id.et_full_name)
        usernameInputLayout = view.findViewById(R.id.username_input_layout)
        usernameEditText = view.findViewById(R.id.et_username)
        emailInputLayout = view.findViewById(R.id.email_input_layout)
        emailEditText = view.findViewById(R.id.et_email)
        confirmButton = view.findViewById(R.id.btn_confirm)
        alreadyHaveAccountTextView = view.findViewById(R.id.tv_already_have_account)

        // Set onClick listener for confirm button
        confirmButton.setOnClickListener {
            performSignUp()
        }

        // Navigate to Sign In page when "Already have account" is clicked
        alreadyHaveAccountTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return view
    }

    private fun performSignUp() {
        val fullName = fullNameEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()

        // Validate input
        if (fullName.isEmpty()) {
            fullNameInputLayout.error = "Nama lengkap tidak boleh kosong"
        } else {
            fullNameInputLayout.error = null
        }

        if (username.isEmpty()) {
            usernameInputLayout.error = "Username tidak boleh kosong"
        } else {
            usernameInputLayout.error = null
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = "Email tidak valid"
        } else {
            emailInputLayout.error = null
        }

        // Check if all inputs are valid before proceeding
        if (fullNameInputLayout.error == null && usernameInputLayout.error == null && emailInputLayout.error == null) {
            // Simulate successful sign-up (replace this with actual sign-up logic)
            Toast.makeText(requireContext(), "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()

            // Show the email verification dialog with the provided email
            showEmailVerificationDialog(email)
        }
    }

    private fun showEmailVerificationDialog(email: String) {
        // Create an instance of SignUpVerifikasiEmailDialog
        val verificationDialog = SignUpVerifikasiEmailDialog()

        // Pass the email to the dialog
        verificationDialog.setEmail(email)

        // Show the dialog
        verificationDialog.show(parentFragmentManager, "SignUpVerifikasiEmailDialog")
    }
}