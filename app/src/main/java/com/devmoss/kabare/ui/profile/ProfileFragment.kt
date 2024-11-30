package com.devmoss.kabare.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentProfileBinding
import com.devmoss.kabare.ui.auth.popups.SocialMediaLinksDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication instance
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if the user is logged in
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // If the user is not logged in, navigate to the SignInFragment
            findNavController().navigate(R.id.action_navigation_profil_to_signInFragment)
        } else {
            // If the user is logged in, show their profile information
            showUserProfile(currentUser)
        }

        // Handle click on the first link container (or any other link)
        binding.llFirstLink.setOnClickListener {
            // Show the dialog fragment
            val dialog = SocialMediaLinksDialog()
            dialog.show(parentFragmentManager, "SocialMediaLinksDialog")
        }

        // Handle click on the settings button
        binding.btnSettings.setOnClickListener {
            // Navigate to Account Settings fragment
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
    }

    private fun showUserProfile(user: FirebaseUser) {
        // You can set the user's profile data here
        // For example, set the user's name and email in the profile layout
//        binding.tvUserName.text = user.displayName
//        binding.tvUserEmail.text = user.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
