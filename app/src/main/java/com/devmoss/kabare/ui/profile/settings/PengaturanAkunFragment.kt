package com.devmoss.kabare.ui.profile.settings

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentPengaturanAkunBinding
import com.devmoss.kabare.ui.auth.popups.KonfirmasiKeluarDialog
import com.devmoss.kabare.ui.profile.settings.viewmodels.PengaturanAkunViewModel

class PengaturanAkunFragment : Fragment() {

    private var _binding: FragmentPengaturanAkunBinding? = null
    private val binding get() = _binding!!

    private val pengaturanAkunViewModel: PengaturanAkunViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe ViewModel data
        observeViewModel()

        // Fetch user data
        pengaturanAkunViewModel.fetchUserData()

        // Setup button and navigation listeners
        setupClickListeners()
    }

    private fun observeViewModel() {
        pengaturanAkunViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvUserName.text = userName
        }

        pengaturanAkunViewModel.userRole.observe(viewLifecycleOwner) { userRole ->
            binding.tvUserRole.text = userRole
        }

        pengaturanAkunViewModel.imgProfile.observe(viewLifecycleOwner) { imgProfile ->
            if (!imgProfile.isNullOrEmpty()) {
                if (imgProfile.startsWith("http")) {
                    Glide.with(this)
                        .load(imgProfile)
                        .circleCrop()
                        .into(binding.imgProfile)
                } else {
                    val imageBytes = Base64.decode(imgProfile, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.imgProfile.setImageBitmap(decodedImage)
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.ic_akun)
            }
        }

        pengaturanAkunViewModel.logoutStatus.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                navigateToIntro()
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            tvUmum.setOnClickListener { navigateTo(R.id.action_pengaturanAkunFragment_to_umumFragment) }
            tvNotifikasi.setOnClickListener { navigateTo(R.id.action_pengaturanAkunFragment_to_settingNotifikasiFragment) }
            tvKeamanan.setOnClickListener { navigateTo(R.id.action_pengaturanAkunFragment_to_keamananFragment) }
            tvBantuan.setOnClickListener { navigateTo(R.id.action_pengaturanAkunFragment_to_pusatBantuanFragment) }
            tvTentang.setOnClickListener { navigateTo(R.id.action_pengaturanAkunFragment_to_tentangFragment) }
            btnLogout.setOnClickListener { showLogoutDialog() }
        }
    }

    private fun showLogoutDialog() {
        if (!isAdded) return
        KonfirmasiKeluarDialog(
            onLogoutComplete = {
                Log.d("LogoutDialog", "onLogoutComplete called")
                navigateToIntro() // Navigate to intro page
            },
            onConfirm = {
                Log.d("LogoutDialog", "Logout confirmed")
                pengaturanAkunViewModel.logout() // Handle any additional logout logic
            },
            onCancel = {
                Log.d("LogoutDialog", "Logout canceled")
                // Optional: Handle cancellation logic
            }
        ).show(childFragmentManager, "KonfirmasiKeluarDialog")
    }

    private fun navigateToIntro() {
        Log.d("PengaturanAkunFragment", "Navigating to IntroFragment")
        val navController = findNavController()
        val currentDestination = navController.currentDestination?.id
        val introDestination = R.id.introFragment

        if (currentDestination != introDestination) {
            Log.d("PengaturanAkunFragment", "Current destination: $currentDestination")
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.settingsFragment, true) // Clear back stack
                .setLaunchSingleTop(true)  // Avoid multiple instances of the same fragment
                .build()

            navController.navigate(R.id.action_pengaturanAkunFragment_to_introFragment, null, navOptions)
        } else {
            Log.d("PengaturanAkunFragment", "Already on IntroFragment")
        }
    }

    private fun navigateTo(destinationId: Int) {
        if (isAdded) {
            findNavController().navigate(destinationId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}