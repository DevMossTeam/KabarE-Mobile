package com.devmoss.kabare.ui.auth

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.devmoss.kabare.R

class LupaPasswordKonfirmasiEmailDialog : DialogFragment() {

    private lateinit var titleView: TextView
    private lateinit var messageView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the layout for the dialog
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_lupa_password_konfirmasi_email, null)

        // Create the AlertDialog using Builder
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true) // Allow the dialog to be cancelable
            .create()

        // Initialize dialog views
        titleView = view.findViewById(R.id.tv_title)
        messageView = view.findViewById(R.id.tv_message)

        // Set button click listener
        val btnOk: Button = view.findViewById(R.id.btn_kirim)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    fun setMessage(message: String) {
        messageView.text = message
    }
}
