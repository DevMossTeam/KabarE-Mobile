package com.devmoss.kabare.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.viewmodels.ResetPasswordViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.navigation.fragment.findNavController

class ResetPasswordFragment : Fragment() {

    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var saveButton: Button

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_password, container, false)

        // Initialize UI components
        initializeUIComponents(view)

        // Observe ViewModel state
        observeViewModel()

        // Set up save button click listener
        saveButton.setOnClickListener {
            handleSavePasswordClick()
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

    private fun observeViewModel() {
        resetPasswordViewModel.resetPasswordState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ResetPasswordViewModel.ResetPasswordState.Success -> {
                    Toast.makeText(requireContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show()
                    // Navigasi ke IntroFragment
                    findNavController().navigate(R.id.action_inputPasswordFragment_to_introFragment)
                }
                is ResetPasswordViewModel.ResetPasswordState.Error -> {
                    Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                is ResetPasswordViewModel.ResetPasswordState.ValidationError -> {
                    newPasswordInputLayout.error = state.newPasswordError
                    confirmPasswordInputLayout.error = state.confirmPasswordError
                }
            }
        })
    }

    private fun handleSavePasswordClick() {
        val newPassword = newPasswordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        resetPasswordViewModel.resetPassword(newPassword, confirmPassword)
    }
}