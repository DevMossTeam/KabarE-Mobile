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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.graphics.BitmapFactory

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
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        // Observe user profile LiveData
        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                updateProfileUI(it)
            } ?: run {
                showSnackbar("Failed to load user profile.")
            }
        })

        // Observe error messages LiveData
        profileViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                showSnackbar(it)
                profileViewModel.clearErrorMessage()
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}