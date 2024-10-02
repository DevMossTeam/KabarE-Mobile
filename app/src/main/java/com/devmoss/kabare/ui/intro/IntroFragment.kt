package com.devmoss.kabare.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.devmoss.kabare.R

class IntroFragment : Fragment() {

    private val viewModel: IntroViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.introStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is IntroViewModel.IntroStatus.Loading -> {
                    // Tampilkan loading (jika perlu)
                }
                is IntroViewModel.IntroStatus.ShowIntro -> {
                    // Tampilkan intro
                }
                is IntroViewModel.IntroStatus.Complete -> {
                    // Navigasi ke halaman berikutnya
                    // Misalnya: findNavController().navigate(R.id.action_introFragment_to_nextFragment)
                }
            }
        }
    }
}