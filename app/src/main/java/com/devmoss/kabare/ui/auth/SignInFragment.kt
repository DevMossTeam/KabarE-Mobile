package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.popups.LupaPasswordKonfirmasiEmailDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var googleSignInButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signUpTextView: TextView

    // Google Sign-In launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), "Google Sign-In Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Initialize UI components
        initializeUIComponents(view)

        // Set up Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Replace with actual ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

        // Set up sign-in button click listener for email sign-in
        signInButton.setOnClickListener {
            performEmailSignIn()
        }

        // Set up Google Sign-In button click listener
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Set up forgot password click listener
        forgotPasswordTextView.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Set up sign-up click listener
        signUpTextView.setOnClickListener {
            navigateToSignUp()
        }

        return view
    }

    private fun initializeUIComponents(view: View) {
        usernameInputLayout = view.findViewById(R.id.username_input_layout)
        usernameEditText = view.findViewById(R.id.et_username)
        passwordInputLayout = view.findViewById(R.id.password_input_layout)
        passwordEditText = view.findViewById(R.id.et_password)
        signInButton = view.findViewById(R.id.btn_masuk)
        googleSignInButton = view.findViewById(R.id.btn_masuk_dengan_email)
        forgotPasswordTextView = view.findViewById(R.id.tv_forgot_password)
        signUpTextView = view.findViewById(R.id.tv_sign_up)
    }

    private fun performEmailSignIn() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate inputs
        if (validateInputs(username, password)) {
            // Simulate sign-in validation logic (replace with real authentication logic)
            if (username == "testuser" && password == "password") {
                Toast.makeText(requireContext(), "Sign In Successful", Toast.LENGTH_SHORT).show()
                // Navigate to the next screen or perform any action on successful sign-in
            } else {
                Toast.makeText(requireContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            usernameInputLayout.error = "Username cannot be empty"
            isValid = false
        } else {
            usernameInputLayout.error = null // Clear error
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            isValid = false
        } else {
            passwordInputLayout.error = null // Clear error
        }

        return isValid
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Google Sign-In succeeded, navigate to a new fragment or activity
                    Toast.makeText(requireContext(), "Sign-In Successful", Toast.LENGTH_SHORT).show()
                    // Redirect to another activity/fragment
                } else {
                    Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showForgotPasswordDialog() {
        // Create and show the Forgot Password Bottom Sheet Dialog
        val lupaPasswordDialog = LupaPasswordKonfirmasiEmailDialog()
        lupaPasswordDialog.show(parentFragmentManager, "LupaPasswordKonfirmasiEmailDialog")
    }

    private fun navigateToSignUp() {
        // Navigate to the Sign Up Fragment
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }
}
