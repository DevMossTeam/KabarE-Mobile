package com.devmoss.kabare.ui.profile

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentProfileBinding
import com.devmoss.kabare.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.graphics.BitmapFactory
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewPager and TabLayout
        setupViewPagerAndTabs()

        // Load user profile data
        profileViewModel.fetchUserProfile()

        // Handle settings navigation
        binding.btnSetting.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        // Navigate to sign-in
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profil_to_signInFragment)
        }

        // Navigate to sign-up
        binding.tvRegisterPrompt.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profil_to_signUpFragment)
        }

        // Observe user profile LiveData
        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                updateProfileUI(it)
            }
        })

        // Observe login status LiveData
        val isLoggedIn = profileViewModel.isUserLoggedIn()  // Use UserRepository method
        handleLoginState(isLoggedIn)

        // Observe error messages LiveData
        profileViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                profileViewModel.clearErrorMessage() // Clear error after displaying it
            }
        })
    }

    private fun setupViewPagerAndTabs() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout
        val adapter = DaftarReaksiViewPager2Adapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Bookmark"
            }
        }.attach()
    }

    private fun updateProfileUI(user: User) {
        binding.tvFullName.text = user.nama_lengkap
        binding.tvUsername.text = user.nama_pengguna
        binding.tvUserStatus.text = user.role

        // Check if profile_pic is a URL or Base64 string
        if (!user.profile_pic.isNullOrEmpty()) {
            if (user.profile_pic.startsWith("http")) {
                // Load image from URL
                Glide.with(this)
                    .load(user.profile_pic)
                    .circleCrop()
                    .into(binding.ivProfilePicture)
            } else {
                // Decode Base64 image string
                val imageBytes = Base64.decode(user.profile_pic, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.ivProfilePicture.setImageBitmap(decodedImage)
            }
        } else {
            // Default image if profile_pic is empty
            binding.ivProfilePicture.setImageResource(R.drawable.ic_akun)
        }
    }

    private fun handleLoginState(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            // Hide Sign-In button and show Edit Profile button
            binding.btnSignIn.visibility = View.GONE
            binding.btnEditProfile.visibility = View.VISIBLE

            // Hide description and show username, user status
            binding.tvDesc.visibility = View.GONE
            binding.tvUsername.visibility = View.VISIBLE
            binding.tvUserStatus.visibility = View.VISIBLE
            binding.tvRegisterPrompt.visibility = View.GONE

            // Handle Edit Profile button click
            binding.btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_umumFragment)
            }

            // Adjust btn_setting constraints to btn_edit_profile
            adjustButtonConstraints(binding.btnEditProfile.id)
        } else {
            // Show Sign-In button and register prompt
            binding.btnSignIn.visibility = View.VISIBLE
            binding.btnEditProfile.visibility = View.GONE

            // Show description and hide username, user status
            binding.tvDesc.visibility = View.VISIBLE
            binding.tvUsername.visibility = View.GONE
            binding.tvUserStatus.visibility = View.GONE
            binding.tvRegisterPrompt.visibility = View.VISIBLE

            // Adjust btn_setting constraints to btn_sign_in
            adjustButtonConstraints(binding.btnSignIn.id)
        }
    }

    private fun adjustButtonConstraints(anchorViewId: Int) {
        val constraintLayout = binding.llSignInSettings as ConstraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Update start-to-end constraint for btn_setting
        constraintSet.connect(
            binding.btnSetting.id,
            ConstraintSet.START,
            anchorViewId,
            ConstraintSet.END,
            8 // Margin
        )

        // Apply updated constraints
        constraintSet.applyTo(constraintLayout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}