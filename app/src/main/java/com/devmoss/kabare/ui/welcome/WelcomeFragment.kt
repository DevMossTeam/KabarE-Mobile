package com.devmoss.kabare.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.devmoss.kabare.R
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

        // Check if the user has seen the welcome screen before
        val sharedPref = requireActivity().getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val hasSeenWelcomePage = sharedPref.getBoolean("hasSeenWelcomePage", false)

        // If the user has already seen the welcome screen, navigate directly to the home
        if (hasSeenWelcomePage) {
            findNavController().navigate(R.id.navigation_home)
            return // Exit early if they have seen it
        }

        // Initialize ViewPager and Adapter
        viewPager = binding.viewPager
        welcomePagerAdapter = WelcomePagerAdapter(this)
        viewPager.adapter = welcomePagerAdapter

        // Update button appearance based on the current page
        updateButtonAppearance(0)

        // Handle Next button click
        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < welcomePagerAdapter.itemCount - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                // User finished welcome pages, save the preference and navigate to home
                with(sharedPref.edit()) {
                    putBoolean("hasSeenWelcomePage", true)
                    apply() // Commit changes
                }
                findNavController().navigate(R.id.navigation_home)
            }
        }

        // Add listener to detect page changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update button appearance when the page is selected
                updateButtonAppearance(position)
            }
        })
    }

    // Function to update button appearance based on the current page
    private fun updateButtonAppearance(position: Int) {
        if (position == welcomePagerAdapter.itemCount - 1) {
            // If it's the last page
            binding.btnNext.apply {
                text = "Bergabung"
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black)) // Make text black for visibility
            }
        } else {
            // For all other pages
            binding.btnNext.apply {
                text = "Selanjutnya"
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // Make text white for visibility
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}