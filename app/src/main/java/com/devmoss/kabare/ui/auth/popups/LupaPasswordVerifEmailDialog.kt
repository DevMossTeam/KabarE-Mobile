package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LupaPasswordVerifEmailDialog : BottomSheetDialogFragment() {

    private lateinit var timerTextView: TextView
    private lateinit var resendTextView: TextView
    private var email: String = "johndoe@gmail.com"  // Default email

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this bottom sheet
        val view = inflater.inflate(R.layout.dialog_lupa_password_verif_email, container, false)

        // Initialize views
        timerTextView = view.findViewById(R.id.tv_timer)
        resendTextView = view.findViewById(R.id.tv_resend)
        val messageTextView = view.findViewById<TextView>(R.id.tv_verif_message)

        // Update the email in the message
        messageTextView.text = "Silahkan buka email anda dan konfirmasi. Kami telah mengirimkan link ke email $email"

        // Start the timer
        startTimer()

        // Set up resend button click listener
        resendTextView.setOnClickListener {
            Toast.makeText(requireContext(), "Mengirim ulang email ke $email", Toast.LENGTH_SHORT).show()
            startTimer() // Restart the timer
        }

        return view
    }

    private fun startTimer() {
        resendTextView.isEnabled = false // Disable resend button during timer
        object : CountDownTimer(120000, 1000) { // 2 minutes timer

            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
                resendTextView.isEnabled = true // Enable resend button when the timer ends
            }
        }.start()
    }

    // Method to set email from the previous dialog
    fun setEmail(email: String) {
        this.email = email
    }
}