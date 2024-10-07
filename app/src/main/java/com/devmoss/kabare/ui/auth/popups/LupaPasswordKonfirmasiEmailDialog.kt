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
import androidx.core.util.PatternsCompat
import androidx.fragment.app.FragmentManager
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class LupaPasswordKonfirmasiEmailDialog : BottomSheetDialogFragment() {

    private lateinit var btnKirim: Button
    private lateinit var btnCancel: TextView
    private lateinit var emailEditText: TextInputEditText
    private lateinit var titleView: TextView
    private lateinit var messageView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this bottom sheet
        val view = inflater.inflate(R.layout.dialog_lupa_password_konfirmasi_email, container, false)

        // Initialize views
        btnKirim = view.findViewById(R.id.btn_kirim)
        btnCancel = view.findViewById(R.id.tv_cancel)
        emailEditText = view.findViewById(R.id.et_email)
        titleView = view.findViewById(R.id.tv_title)
        messageView = view.findViewById(R.id.tv_message)

        // Set up send button click listener
        btnKirim.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                // Show verification dialog after successful email confirmation
                openVerificationDialog(email)
            } else {
                emailEditText.error = "Please enter a valid email address"
            }
        }

        // Set up cancel button click listener
        btnCancel.setOnClickListener {
            dismiss() // Dismiss the dialog
        }

        // Show keyboard automatically when dialog is opened
        view.post {
            emailEditText.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(emailEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        return view
    }

    // Open the LupaPasswordVerifEmailDialog after email is confirmed
    private fun openVerificationDialog(email: String) {
        // Dismiss current dialog before opening new one
        dismiss()

        // Create and show the verification dialog
        val verifEmailDialog = LupaPasswordVerifEmailDialog().apply {
            setEmail(email) // Pass the email to verification dialog
        }
        verifEmailDialog.show(parentFragmentManager, "LupaPasswordVerifEmailDialog")
    }

    // Optionally add methods to customize title and message from SignInFragment
    fun setTitle(title: String) {
        titleView.text = title
    }

    fun setMessage(message: String) {
        messageView.text = message
    }
}