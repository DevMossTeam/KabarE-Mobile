package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentTentangBinding

class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listener for tvLicense
        binding.tvLicense.setOnClickListener {
            // Navigate to LicenseFragment
            findNavController().navigate(R.id.action_tentangFragment_to_licenseFragment) // Ensure this ID matches your navigation graph
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks by nullifying the binding when the view is destroyed
        _binding = null
    }
}