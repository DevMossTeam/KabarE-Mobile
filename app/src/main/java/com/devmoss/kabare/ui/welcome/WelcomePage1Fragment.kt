package com.devmoss.kabare.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.MainActivity
import com.devmoss.kabare.databinding.FragmentWelcomePage1Binding

class WelcomePage1Fragment : Fragment() {
    private var _binding: FragmentWelcomePage1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        _binding = FragmentWelcomePage1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for navigation
        binding.btnNext.setOnClickListener {
            navigateToNextPage()
        }

        binding.skipText.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToNextPage() {
        findNavController().navigate(R.id.action_welcomePage1Fragment_to_welcomePage2Fragment)
    }

    private fun navigateToMainActivity() {
        // Start MainActivity using Intent
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Close the current activity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}