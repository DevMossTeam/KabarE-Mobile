package com.devmoss.kabare.ui.auth.popups

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.auth.viewmodels.SignUpViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SignUpVerifikasiEmailDialog : BottomSheetDialogFragment() {

    private lateinit var timerTextView: TextView
    private lateinit var resendTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private var email: String? = null
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var otpInputs: List<EditText>
    private lateinit var sharedPreferences: SharedPreferences
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_sign_up_verifikasi_email, container, false)

        // Initialize Views
        timerTextView = view.findViewById(R.id.tv_timer)
        resendTextView = view.findViewById(R.id.tv_resend)
        val messageTextView = view.findViewById<TextView>(R.id.tv_instruction)
        loadingProgressBar = view.findViewById(R.id.progressBar)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        // Initialize OTP Input fields
        otpInputs = listOf(
            view.findViewById(R.id.et_otp_1),
            view.findViewById(R.id.et_otp_2),
            view.findViewById(R.id.et_otp_3),
            view.findViewById(R.id.et_otp_4),
            view.findViewById(R.id.et_otp_5),
            view.findViewById(R.id.et_otp_6)
        )

        setupOtpInputListeners()

        // Show message based on email
        messageTextView.text = email?.let {
            "Silahkan buka email Anda dan masukkan OTP yang telah dikirimkan ke $it."
        } ?: "Email tidak ditemukan, silakan coba lagi."

        // Start the timer
        startTimer()

        // Set resend OTP listener
        setupResendListener()

        // Prevent dialog from closing when clicked outside
        isCancelable = false

        return view
    }

    private fun setupOtpInputListeners() {
        for (i in otpInputs.indices) {
            otpInputs[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length == 1) {
                        if (i < otpInputs.size - 1) {
                            otpInputs[i + 1].requestFocus()
                        }
                    }
                    if (otpInputs.all { it.text.isNotEmpty() }) {
                        submitOtp()
                    }
                }

                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {}
            })
        }
    }

    private fun setupResendListener() {
        resendTextView.setOnClickListener {
            email?.let {
                resendVerificationEmail()
            } ?: run {
                Toast.makeText(requireContext(), "Email tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTimer() {
        signUpViewModel.otpExpiry.observe(viewLifecycleOwner) { otpExpiry ->
            if (otpExpiry == 0L) {
                // Tidak ada waktu kedaluwarsa yang valid
                resendTextView.isEnabled = true
                return@observe
            }

            val currentTime = System.currentTimeMillis()
            val timeRemaining = otpExpiry - currentTime

            if (timeRemaining <= 0) {
                // OTP expired
                Toast.makeText(
                    requireContext(),
                    "OTP telah kedaluwarsa. Mohon kirim ulang OTP.",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            }

            // Countdown Timer
            countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = (millisUntilFinished / 60000) % 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    timerTextView.text = String.format("%02d:%02d", minutes, seconds)
                }
                override fun onFinish() {
                }
            }.start()

            resendTextView.isEnabled = false
        }
    }

    private fun resendVerificationEmail() {
        loadingProgressBar.visibility = View.VISIBLE
        resendTextView.isEnabled = false

        signUpViewModel.resendOtp()
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SignUpViewModel.AuthStatus.SUCCESS -> {
                    Toast.makeText(requireContext(), "OTP telah dikirim ulang ke $email", Toast.LENGTH_SHORT).show()

                    // Save new expiry time (5 minutes from now)
                    val newExpiryTime = System.currentTimeMillis() + (5 * 60 * 1000) // 5 minutes
                    sharedPreferences.edit().putLong("otp_expiry", newExpiryTime).apply()

                    startTimer() // Restart the timer
                }
                SignUpViewModel.AuthStatus.FAILURE -> {
                    Toast.makeText(requireContext(), result.message ?: "Gagal mengirim ulang OTP.", Toast.LENGTH_SHORT).show()
                    resendTextView.isEnabled = true
                }
                else -> {}
            }
            loadingProgressBar.visibility = View.GONE
        }
    }

    private fun submitOtp() {
        val enteredOtp = otpInputs.joinToString("") { it.text.toString() }
        if (enteredOtp.length == 6) {
            signUpViewModel.verifyOtp(enteredOtp)
            signUpViewModel.otpValidationResult.observe(viewLifecycleOwner) { result ->
                when (result.status) {
                    SignUpViewModel.AuthStatus.SUCCESS -> {
                        Toast.makeText(requireContext(), "OTP berhasil diverifikasi!", Toast.LENGTH_SHORT).show()

                        // Navigasi ke SignUpInputFragment
                        findNavController().navigate(R.id.action_signUpVerifikasiEmailDialog_to_signUpInputFragment)

                        // Tutup dialog
                        dismiss()
                    }
                    SignUpViewModel.AuthStatus.FAILURE -> {
                        Toast.makeText(requireContext(), result.message ?: "OTP tidak valid.", Toast.LENGTH_SHORT).show()
                        clearOtpInputs()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun clearOtpInputs() {
        otpInputs.forEach { it.text.clear() }
        otpInputs[0].requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }

    fun setEmail(email: String) {
        this.email = email
    }
}