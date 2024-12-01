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
import com.devmoss.kabare.ui.auth.viewmodels.LupaPasswordViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LupaPasswordVerifEmailDialog : BottomSheetDialogFragment() {

    private lateinit var timerTextView: TextView
    private lateinit var resendTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private var email: String? = null
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var otpInputs: List<EditText>
    private lateinit var sharedPreferences: SharedPreferences
    private val lupaPasswordViewModel: LupaPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_lupa_password_verif_email, container, false)

        // Initialize Views
        timerTextView = view.findViewById(R.id.tv_timer)
        resendTextView = view.findViewById(R.id.tv_resend)
        val messageTextView = view.findViewById<TextView>(R.id.tv_instruction)
        loadingProgressBar = view.findViewById(R.id.progressBar)
        resendTextView.isEnabled = false
        updateResendTextViewState(false)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("LupaPasswordPrefs", Context.MODE_PRIVATE)

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
            "Silahkan masukkan OTP yang telah dikirimkan ke email Anda: $it."
        } ?: "Email tidak ditemukan. Coba lagi."

        // Start timer for OTP validity
        startTimer()

        // Set resend OTP listener
        setupResendListener()

        // Prevent dialog from closing when clicked outside
        isCancelable = false

        // Observe results from ViewModel once, on start
        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        lupaPasswordViewModel.otpValidationResult.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                LupaPasswordViewModel.AuthStatus.SUCCESS -> {
                    Toast.makeText(requireContext(), "OTP berhasil diverifikasi!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_lupaPasswordVerifEmailDialog_to_resetPasswordFragment)
                    dismiss()
                }
                LupaPasswordViewModel.AuthStatus.FAILURE -> {
                    Toast.makeText(requireContext(), result.message ?: "OTP tidak valid.", Toast.LENGTH_SHORT).show()
                    clearOtpInputs()
                }
                else -> {}
            }
        }

        lupaPasswordViewModel.emailVerificationResult.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                LupaPasswordViewModel.AuthStatus.SUCCESS -> {
                    startTimer()
                }
                LupaPasswordViewModel.AuthStatus.FAILURE -> {
                    Toast.makeText(requireContext(), result.message ?: "Gagal mengirim OTP.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
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

                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
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
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }

        val currentTime = System.currentTimeMillis()
        val otpExpiry = sharedPreferences.getLong("otp_expiry", 0L)

        if (otpExpiry == 0L || otpExpiry <= currentTime) {
            timerTextView.text = "00:00"
            resendTextView.isEnabled = true
            updateResendTextViewState(true)
            return
        }

        val timeRemaining = otpExpiry - currentTime

        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 60000) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
                resendTextView.isEnabled = true
                updateResendTextViewState(true)
            }
        }.start()

        resendTextView.isEnabled = false
        updateResendTextViewState(false)
    }

    private fun updateResendTextViewState(isEnabled: Boolean) {
        resendTextView.apply {
            this.isEnabled = isEnabled
            setTextColor(
                if (isEnabled) resources.getColor(R.color.colorPrimary, null)
                else resources.getColor(R.color.gray, null)
            )
        }
    }

    private fun resendVerificationEmail() {
        loadingProgressBar.visibility = View.VISIBLE
        resendTextView.isEnabled = false

        lupaPasswordViewModel.resendOtp()
        lupaPasswordViewModel.passwordResetResult.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                LupaPasswordViewModel.AuthStatus.SUCCESS -> {
                    Toast.makeText(requireContext(), "OTP telah dikirim ulang ke $email", Toast.LENGTH_SHORT).show()
                    val newExpiryTime = System.currentTimeMillis() + (5 * 60 * 1000)
                    sharedPreferences.edit().putLong("otp_expiry", newExpiryTime).apply()
                    startTimer()
                }
                LupaPasswordViewModel.AuthStatus.FAILURE -> {
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
            lupaPasswordViewModel.verifyOtp(enteredOtp)
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