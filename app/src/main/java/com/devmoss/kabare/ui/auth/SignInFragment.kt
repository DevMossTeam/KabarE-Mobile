package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignInFragment : DialogFragment() {

    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signUpTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Initialize UI components
        usernameInputLayout = view.findViewById(R.id.username_input_layout)
        usernameEditText = view.findViewById(R.id.et_username)
        passwordInputLayout = view.findViewById(R.id.password_input_layout)
        passwordEditText = view.findViewById(R.id.et_password)
        signInButton = view.findViewById(R.id.btn_masuk)
        forgotPasswordTextView = view.findViewById(R.id.tv_forgot_password)
        signUpTextView = view.findViewById(R.id.tv_sign_up)

        // Set up sign-in button click listener
        signInButton.setOnClickListener {
            performSignIn()
        }

        // Set up forgot password click listener
        forgotPasswordTextView.setOnClickListener {
            // Navigate to the Lupa Password Dialog
            findNavController().navigate(R.id.lupaPasswordKonfirmasiEmailDialog)
        }

        // Set up sign up click listener
        signUpTextView.setOnClickListener {
            // Handle sign up action
            Toast.makeText(requireContext(), "Sign Up Clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun performSignIn() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate inputs
        if (username.isEmpty()) {
            usernameInputLayout.error = "Username cannot be empty"
            return
        } else {
            usernameInputLayout.error = null // Clear error
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            return
        } else {
            passwordInputLayout.error = null // Clear error
        }

        // Perform sign-in logic (this could be a network call to your backend)
        if (username == "testuser" && password == "password") { // Replace with your actual validation
            Toast.makeText(requireContext(), "Sign In Successful", Toast.LENGTH_SHORT).show()
            // Navigate to the next screen or perform any action on successful sign in
        } else {
            Toast.makeText(requireContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show()
        }
    }
}
