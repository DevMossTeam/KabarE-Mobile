package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.devmoss.kabare.databinding.FragmentWelcomeBinding
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
        welcomePagerAdapter = WelcomePagerAdapter(this) // Using 'this' to get the Fragment
        viewPager.adapter = welcomePagerAdapter

        // Handle Next button click
        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < welcomePagerAdapter.itemCount - 1) {
                viewPager.currentItem += 1 // Move to the next page
            } else {
                navigateToNextScreen()
            }
        }

        // Optional: Add listener to detect page changes if needed
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Optionally update UI elements based on the selected page
            }
        })
    }

    private fun navigateToNextScreen() {
        // Implement your navigation logic here
        // Example: findNavController().navigate(R.id.action_welcomeFragment_to_nextFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}