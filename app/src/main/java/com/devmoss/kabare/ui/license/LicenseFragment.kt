package com.devmoss.kabare.ui.license

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devmoss.kabare.databinding.FragmentLicenseBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LicenseFragment : Fragment() {
    private var _binding: FragmentLicenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLicenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the license text from the assets folder
        loadLicenseText()
    }

    private fun loadLicenseText() {
        try {
            // Log before trying to open the asset
            Log.d("LicenseFragment", "Attempting to open license file.")

            val inputStream = requireContext().assets.open("LICENSE")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            reader.use {
                it.forEachLine { line ->
                    stringBuilder.append(line).append("\n")
                }
            }
            binding.tvLicenseText.text = stringBuilder.toString()
        } catch (e: IOException) {
            Log.e("LicenseFragment", "Error loading license file: ${e.message}")
            binding.tvLicenseText.text = "Error loading license."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}