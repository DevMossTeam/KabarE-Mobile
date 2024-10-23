package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.popups.ChangePasswordDialog // Import dialog for changing password
import com.devmoss.kabare.ui.auth.popups.KonfirmasiUbahEmail // Import dialog to confirm email change
import com.devmoss.kabare.ui.auth.popups.LupaPasswordKonfirmasiEmailDialog

class KeamananFragment : Fragment() {

    private lateinit var tvEmailDisplay: TextView
    private lateinit var etEmailEdit: EditText
    private lateinit var btnEditEmail: ImageView
    private lateinit var btnSaveEmail: TextView
    private lateinit var btnCancelEmail: TextView
    private lateinit var llEmailDisplay: LinearLayout
    private lateinit var llEmailEdit: LinearLayout
    private lateinit var llLupaKataSandi: LinearLayout
    private lateinit var llGantiPassword: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keamanan, container, false)

        // Initialize views
        tvEmailDisplay = view.findViewById(R.id.tv_email_display)
        etEmailEdit = view.findViewById(R.id.et_email)
        btnEditEmail = view.findViewById(R.id.img_edit_email)
        btnSaveEmail = view.findViewById(R.id.btn_simpan)
        btnCancelEmail = view.findViewById(R.id.btn_batal)
        llEmailDisplay = view.findViewById(R.id.ll_email_display)
        llEmailEdit = view.findViewById(R.id.ll_email_edit)
        llLupaKataSandi = view.findViewById(R.id.llLupaKataSandi)
        llGantiPassword = view.findViewById(R.id.llGantiPassword)

        // Set onClickListeners for edit, save, cancel buttons, and ganti password
        btnEditEmail.setOnClickListener { showEditEmail() }
        btnSaveEmail.setOnClickListener { showConfirmChangeEmailDialog() } // Show confirmation dialog
        btnCancelEmail.setOnClickListener { cancelEditEmail() }

        llLupaKataSandi.setOnClickListener {
            showForgotPasswordDialog()
        }

        llGantiPassword.setOnClickListener {
            showChangePasswordDialog() // Show change password dialog
        }

        return view
    }

    // Show the edit email layout and hide the static email view
    private fun showEditEmail() {
        llEmailDisplay.visibility = View.GONE
        llEmailEdit.visibility = View.VISIBLE
        etEmailEdit.setText(tvEmailDisplay.text.toString())
        etEmailEdit.requestFocus()
    }

    // Show the confirmation dialog for email change
    private fun showConfirmChangeEmailDialog() {
        val newEmail = etEmailEdit.text.toString().trim()

        // Validate email format
        if (isValidEmail(newEmail)) {
            val konfirmasiDialog = KonfirmasiUbahEmail()
            konfirmasiDialog.setOnEmailChangeConfirmed {
                // Update email in the display after confirmation
                tvEmailDisplay.text = newEmail
                showToast(R.string.email_updated) // Success message
                cancelEditEmail() // Switch back to the display mode
            }
            konfirmasiDialog.show(parentFragmentManager, "KonfirmasiUbahEmail")
        } else {
            showToast(R.string.email_invalid) // Error message for invalid email
        }
    }

    // Cancel editing and revert to static email view
    private fun cancelEditEmail() {
        llEmailEdit.visibility = View.GONE
        llEmailDisplay.visibility = View.VISIBLE
        etEmailEdit.text.clear() // Clear the input field
    }

    // Email validation method
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Helper function to show toast messages
    private fun showToast(messageId: Int) {
        Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
    }

    private fun showForgotPasswordDialog() {
        // Create and show the Forgot Password Bottom Sheet Dialog
        val lupaPasswordDialog = LupaPasswordKonfirmasiEmailDialog()
        lupaPasswordDialog.show(parentFragmentManager, "LupaPasswordKonfirmasiEmailDialog")
    }

    // Function to show Change Password Dialog
    private fun showChangePasswordDialog() {
        // Create and show the Change Password Bottom Sheet Dialog
        val changePasswordDialog = ChangePasswordDialog()
        changePasswordDialog.show(parentFragmentManager, "ChangePasswordDialog")
    }
}