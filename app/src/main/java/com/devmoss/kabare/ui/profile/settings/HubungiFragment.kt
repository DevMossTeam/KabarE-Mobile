package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.profile.settings.viewmodels.HubungiViewModel

class HubungiFragment : Fragment() {

    private lateinit var etComplaint: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvAdditionalInfo: TextView
    private val hubungiViewModel: HubungiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hubungi, container, false)

        // Initialize UI elements
        etComplaint = view.findViewById(R.id.etComplaint)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvAdditionalInfo = view.findViewById(R.id.tvAdditionalInfo)

        // Observe ViewModel to set button state and additional info
        observeViewModel()

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            val complaintText = etComplaint.text.toString().trim()

            if (complaintText.isEmpty()) {
                Toast.makeText(requireContext(), "Harap masukkan keluhan anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call ViewModel to submit complaint
            hubungiViewModel.submitComplaint(complaintText, {
                // On success
                Toast.makeText(requireContext(), "Keluhan berhasil dikirim", Toast.LENGTH_SHORT).show()
                etComplaint.text.clear() // Clear input after success
            }, { errorMessage ->
                // On failure
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            })
        }
        return view
    }

    private fun observeViewModel() {
        hubungiViewModel.isUserLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            btnSubmit.isEnabled = isLoggedIn // Enable or disable button based on login status
            if (!isLoggedIn) {
                tvAdditionalInfo.text = "Login terlebih dahulu atau hubungi kami lewat email devmossteam@gmail.com"
                btnSubmit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
            } else {
                tvAdditionalInfo.text = "Silakan masukkan keluhan anda di bawah ini."
                btnSubmit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        }
    }
}