package com.devmoss.kabare.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IntroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the introStatus from the ViewModel
        viewModel.introStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is IntroViewModel.IntroStatus.Loading -> {
                    // Handle loading state if needed
                }
                is IntroViewModel.IntroStatus.ShowIntro -> {
                    // Show the intro UI here if needed
                }
                is IntroViewModel.IntroStatus.NavigateToWelcome -> {
                    // Navigate to Welcome Page 1 Fragment
                    findNavController().navigate(R.id.action_introFragment_to_welcomePage1Fragment) // Update this line
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
