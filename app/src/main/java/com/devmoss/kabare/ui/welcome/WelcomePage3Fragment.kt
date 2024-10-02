package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentWelcomePage3Binding

class WelcomePage3Fragment : Fragment() {
    private var _binding: FragmentWelcomePage3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomePage3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            navigateToHomeFragment()
        }

        // Animate the third dot
        animateDot(binding.dot3)
        // Reset other dots
        resetDot(binding.dot1)
        resetDot(binding.dot2)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_welcomePage3Fragment_to_homeFragment)
    }

    private fun animateDot(dot: View) {
        dot.animate()
            .scaleX(1.75f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun resetDot(dot: View) {
        dot.scaleX = 1f
        dot.scaleY = 1f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
