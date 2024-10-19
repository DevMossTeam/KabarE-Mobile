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
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the introStatus from the ViewModel
        viewModel.introStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is IntroViewModel.IntroStatus.Loading -> {
                    // Optionally show a loading indicator
                }
                is IntroViewModel.IntroStatus.ShowIntro -> {
                    // Optionally show the intro UI
                }
                is IntroViewModel.IntroStatus.NavigateToWelcome -> {
                    // Navigate to Welcome Fragment
                    findNavController().navigate(R.id.action_introFragment_to_welcomeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by nullifying the binding reference
    }
}