package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentSignUpBinding
import com.devmoss.kabare.ui.auth.popups.SignUpVerifikasiEmailDialog
import com.devmoss.kabare.ui.auth.viewmodels.SignUpViewModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    private var isProcessing = false // Prevent double clicks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        setupBackButton()
    }

    private fun setupUI() {
        // Handle register button click
        binding.btnConfirm.setOnClickListener {
            if (isProcessing) return@setOnClickListener // Prevent double clicks

            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val fullName = binding.etFullName.text.toString().trim()

            if (validateInput(email, username, fullName)) {
                isProcessing = true // Mark as processing
                showLoading(true)
                viewModel.register(email, username, fullName)
            }
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            if (!isProcessing) {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    private fun observeViewModel() {
        // Observe signUpResult to handle registration result
        viewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                handleSignUpResult(it.status, it.message)
            }
        }
    }

    private fun handleSignUpResult(status: SignUpViewModel.AuthStatus, message: String?) {
        when (status) {
            SignUpViewModel.AuthStatus.SUCCESS -> {
                // Handle successful registration by showing OTP dialog
                val email = binding.etEmail.text.toString().trim()
                showEmailVerificationDialog(email)
            }
            SignUpViewModel.AuthStatus.FAILURE -> {
                // Show error message
                Toast.makeText(requireContext(), message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                resetState()
            }
            SignUpViewModel.AuthStatus.LOADING -> {
                // Loading state is already handled by showLoading()
            }
        }
    }

    private fun showEmailVerificationDialog(email: String) {
        val verificationDialog = SignUpVerifikasiEmailDialog().apply {
            setEmail(email)
        }
        verificationDialog.show(parentFragmentManager, "SignUpVerifikasiEmailDialog")
    }

    private fun validateInput(email: String, username: String, fullName: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.emailInputLayout.error = "Email required"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        if (username.isEmpty()) {
            binding.usernameInputLayout.error = "Username required"
            isValid = false
        } else {
            binding.usernameInputLayout.error = null
        }

        if (fullName.isEmpty()) {
            binding.fullNameInputLayout.error = "Full name required"
            isValid = false
        } else {
            binding.fullNameInputLayout.error = null
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        binding.btnConfirm.apply {
            isEnabled = !isLoading
            setBackgroundColor(
                if (isLoading)
                    requireContext().getColor(R.color.button_disabled)
                else
                    requireContext().getColor(R.color.button_enabled)
            )
            setTextColor(
                if (isLoading)
                    requireContext().getColor(R.color.black)
                else
                    requireContext().getColor(R.color.white)
            )
        }

        binding.etEmail.isEnabled = !isLoading
        binding.etUsername.isEnabled = !isLoading
        binding.etFullName.isEnabled = !isLoading
    }

    private fun resetState() {
        isProcessing = false // Allow interaction again
        showLoading(false)
    }

    private fun setupBackButton() {
        // Handle Toolbar back button
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar) // Sesuaikan ID toolbar
        toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}