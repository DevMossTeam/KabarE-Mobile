package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.popups.LupaPasswordKonfirmasiEmailDialog
import com.devmoss.kabare.ui.auth.viewmodels.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignInFragment : Fragment() {

    private val signInViewModel: SignInViewModel by activityViewModels()

    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var googleSignInButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signUpTextView: TextView

    private lateinit var googleSignInClient: GoogleSignInClient

    // Google Sign-In launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Log.d("SignInFragment", "Google Sign-In intent received")
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("SignInFragment", "Google account retrieved: ${account.email}")
                    signInViewModel.firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.e("SignInFragment", "Google Sign-In failed: ${e.message}")
                    signInViewModel.showToast.value = getString(R.string.google_sign_in_failed, e.message)
                }
            } else {
                Log.d("SignInFragment", "Google Sign-In intent cancelled or failed")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Initialize UI components
        initializeUIComponents(view)

        // Initialize Google Sign-In client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), signInViewModel.getGoogleSignInOptions())

        // Observe LiveData dari ViewModel
        observeViewModel()

        // Set up sign-in button click listener for email sign-in
        signInButton.setOnClickListener { performEmailSignIn() }

        // Set up Google Sign-In button click listener
        googleSignInButton.setOnClickListener { signInWithGoogle() }

        // Set up forgot password click listener
        forgotPasswordTextView.setOnClickListener { showForgotPasswordDialog() }

        // Set up sign-up click listener
        signUpTextView.setOnClickListener { navigateToSignUp() }

        // Set up back button handling for Toolbar and device back button
        setupBackButton()

        return view
    }

    private fun observeViewModel() {
        // Observe sign-in status
        signInViewModel.signInStatus.observe(viewLifecycleOwner, Observer { status ->
            if (status == "success") navigateToHome()
        })

        // Observe toast messages
        signInViewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // Observe navigation actions
        signInViewModel.navigationAction.observe(viewLifecycleOwner, Observer { action ->
            when (action) {
                "navigate_to_intro" -> navigateToHome()
                "navigate_to_sign_up_input" -> navigateToSignUpInput()
            }
        })
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

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.username_or_password_empty), Toast.LENGTH_SHORT).show()
            return
        }

        signInViewModel.signInWithApi(username, password)
    }

    private fun signInWithGoogle() {
        Log.d("SignInFragment", "Launching Google Sign-In")
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)

    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_signInFragment_to_introFragment)
    }

    private fun showForgotPasswordDialog() {
        val lupaPasswordDialog = LupaPasswordKonfirmasiEmailDialog()
        lupaPasswordDialog.show(parentFragmentManager, "LupaPasswordKonfirmasiEmailDialog")
    }

    private fun navigateToSignUp() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun navigateToSignUpInput() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpInputFragment)
    }

    private fun setupBackButton() {
        // Handle Toolbar back button
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar) // Sesuaikan ID toolbar
        toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_introFragment)
        }

        // Handle device back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signInFragment_to_introFragment)
            }
        })
    }
}