package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R

class PusatBantuanFragment : Fragment() {

    private lateinit var tvTentang: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pusat_bantuan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the LinearLayout
        tvTentang = view.findViewById(R.id.tvTentang)

        // Set a click listener on the LinearLayout
        tvTentang.setOnClickListener {
            navigateToHubungiFragment()
        }
    }

    private fun navigateToHubungiFragment() {
        // Navigate to HubungiFragment using the navigation controller
        findNavController().navigate(R.id.action_pusatBantuanFragment_to_hubungiFragment)
    }
}