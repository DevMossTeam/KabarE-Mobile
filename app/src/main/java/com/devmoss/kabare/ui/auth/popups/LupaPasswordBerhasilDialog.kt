package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LupaPasswordBerhasilDialog : BottomSheetDialogFragment() {

    private lateinit var signInTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.dialog_lupa_password_berhasil, container, false)

        // Initialize views
        signInTextView = view.findViewById(R.id.tv_sign_in)

        // Set click listener for Sign In
        signInTextView.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Sign In", Toast.LENGTH_SHORT).show()
            dismiss() // Close the dialog or navigate to the sign-in page
        }

        return view
    }
}