package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KonfirmasiUbahEmail : BottomSheetDialogFragment() {

    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: TextView
    private var onEmailChangeConfirmed: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.dialog_konfirmasi_ubah_email, container, false)

        // Initialize views
        btnConfirm = view.findViewById(R.id.btn_change_email)
        btnCancel = view.findViewById(R.id.tv_cancel)

        // Set up button click listeners
        btnConfirm.setOnClickListener {
            onEmailChangeConfirmed?.invoke() // Invoke the confirmation listener
            dismiss() // Dismiss the dialog
        }

        btnCancel.setOnClickListener {
            dismiss() // Dismiss the dialog
        }

        return view
    }

    // Method to set the listener for confirmation
    fun setOnEmailChangeConfirmed(listener: () -> Unit) {
        onEmailChangeConfirmed = listener
    }
}