package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SocialMediaLinksDialog : BottomSheetDialogFragment() {

    private lateinit var link1Container: LinearLayout
    private lateinit var link2Container: LinearLayout
    private lateinit var link3Container: LinearLayout
    private lateinit var link4Container: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.dialog_social_media_links, container, false)

        // Initialize the views
        link1Container = view.findViewById(R.id.link1_container)
        link2Container = view.findViewById(R.id.link2_container)
        link3Container = view.findViewById(R.id.link3_container)
        link4Container = view.findViewById(R.id.link4_container)

        // Set click listeners for each link
        link1Container.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Link 1", Toast.LENGTH_SHORT).show()
        }

        link2Container.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Link 2", Toast.LENGTH_SHORT).show()
        }

        link3Container.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Link 3", Toast.LENGTH_SHORT).show()
        }

        link4Container.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Link 4", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
