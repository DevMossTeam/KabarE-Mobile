// WelcomeFragment.kt
package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentWelcomeBinding
import devmoss.kabare.ui.welcome.WelcomePagerAdapter
import devmoss.kabare.ui.welcome.WelcomeViewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private lateinit var welcomePagerAdapter: WelcomePagerAdapter
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewPager
        viewPager = binding.viewPager
        welcomePagerAdapter = WelcomePagerAdapter(requireActivity())
        viewPager.adapter = welcomePagerAdapter

        // Handle Next button click
        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < welcomePagerAdapter.itemCount - 1) {
                viewPager.currentItem = viewPager.currentItem + 1 // Move to the next page
            } else {
                // If on the last page, you can navigate to another screen or finish the intro
                navigateToNextScreen()
            }
        }

        // Optional: Add a listener to detect page changes if needed
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update UI or perform actions based on the selected page
            }
        })
    }

    private fun navigateToNextScreen() {
        // Implement navigation logic here, e.g., using NavController
        // Example: findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}