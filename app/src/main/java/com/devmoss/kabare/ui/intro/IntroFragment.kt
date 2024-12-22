package com.devmoss.kabare.ui.intro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IntroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)

        viewModel.introStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is IntroViewModel.IntroStatus.Loading -> {
                    // Handle loading
                }
                is IntroViewModel.IntroStatus.ShowIntro -> {
                    // Show intro screen if necessary
                }
                is IntroViewModel.IntroStatus.NavigateToWelcome -> {
                    // Skip intro and directly navigate to the Welcome page
                    findNavController().navigate(R.id.action_introFragment_to_welcomeFragment)
                }
            }
        }

        viewModel.deviceToken.observe(viewLifecycleOwner) { token ->
            Log.d("IntroFragment", "Device Token: $token")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}