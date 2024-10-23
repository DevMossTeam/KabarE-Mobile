package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R

class HubungiFragment : Fragment() {

    private lateinit var etComplaint: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvAdditionalInfo: TextView

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

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            submitComplaint()
        }

        return view
    }

    private fun submitComplaint() {
        val complaintText = etComplaint.text.toString().trim()
        if (complaintText.isEmpty()) {
            Toast.makeText(requireContext(), "Harap masukkan keluhan anda", Toast.LENGTH_SHORT).show()
            return
        }

        // Here, you can handle the complaint submission (e.g., send to server, save locally, etc.)
        // For now, we will just show a toast message
        Toast.makeText(requireContext(), "Keluhan berhasil dikirim", Toast.LENGTH_SHORT).show()

        // Clear the input field after submission
        etComplaint.text.clear()
    }
}