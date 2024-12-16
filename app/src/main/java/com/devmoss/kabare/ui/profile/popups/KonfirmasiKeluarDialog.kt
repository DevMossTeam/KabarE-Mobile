package com.devmoss.kabare.ui.auth.popups

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.devmoss.kabare.R
import com.devmoss.kabare.data.repository.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KonfirmasiKeluarDialog(
    private val onLogoutComplete: () -> Unit, // Callback after logout is complete
    private val onConfirm: () -> Unit,       // Callback for "Yes" action
    private val onCancel: () -> Unit         // Callback for "No" action
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_konfirmasi_keluar, container, false)

        // Initialize buttons
        val confirmButton: Button = view.findViewById(R.id.button_ya)
        val cancelButton: TextView = view.findViewById(R.id.button_tidak)

        // Handle "Yes" button click for logout
        confirmButton.setOnClickListener {
            performLogout(requireContext()) // Perform the logout operation
            onConfirm()                     // Notify that logout logic is triggered
            onLogoutComplete()              // Callback to indicate logout is finished
            dismiss()                       // Close the dialog
        }

        // Handle "No" button click to dismiss the dialog
        cancelButton.setOnClickListener {
            onCancel() // Notify cancel action
            dismiss()  // Close the dialog
        }

        return view
    }

    // Perform logout using UserRepository
    private fun performLogout(context: Context) {
        val userRepository = UserRepository(context)
        userRepository.clearUserData() // Clear user data
        userRepository.saveLoginStatus(false) // Set login status to false
        Log.d("LogoutDialog", "User data cleared and logout status updated")
    }
}