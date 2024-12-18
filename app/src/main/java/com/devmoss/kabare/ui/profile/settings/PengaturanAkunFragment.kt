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

    private val viewModel: PengaturanAkunViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.fetchUserData()
        viewModel.checkUserLoginStatus() // Cek status login saat fragment dimuat
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.apply {
            userName.observe(viewLifecycleOwner) { name ->
                binding.tvUserName.text = name
            }

            userRole.observe(viewLifecycleOwner) { role ->
                binding.tvUserRole.text = role
            }

            imgProfile.observe(viewLifecycleOwner) { imgProfile ->
                loadImageProfile(imgProfile)
            }

            isUserLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
                setVisibilityForLoginStatus(isLoggedIn)
            }

            logoutStatus.observe(viewLifecycleOwner) { isLoggedOut ->
                if (isLoggedOut) navigateToIntro()
            }
        }
    }

    private fun loadImageProfile(imgProfile: String?) {
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

    private fun setVisibilityForLoginStatus(isLoggedIn: Boolean) {
        with(binding) {
            tvUmum.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
            tvKeamanan.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
            btnLogout.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
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
                navigateToIntro()
            },
            onConfirm = {
                Log.d("LogoutDialog", "Logout confirmed")
                viewModel.logout()
            },
            onCancel = {
                Log.d("LogoutDialog", "Logout canceled")
            }
        ).show(childFragmentManager, "KonfirmasiKeluarDialog")
    }

    private fun navigateToIntro() {
        Log.d("PengaturanAkunFragment", "Navigating to IntroFragment")
        val navController = findNavController()
        val currentDestination = navController.currentDestination?.id
        val introDestination = R.id.introFragment

        if (currentDestination == introDestination) {
            Log.d("PengaturanAkunFragment", "Already on IntroFragment")
            return // Hindari navigasi berulang
        }

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.settingsFragment, true) // Clear back stack
            .setLaunchSingleTop(true) // Avoid multiple instances
            .build()

        navController.navigate(R.id.action_pengaturanAkunFragment_to_introFragment, null, navOptions)
    }

    private fun navigateTo(destinationId: Int) {
        if (isAdded) {
            findNavController().navigate(destinationId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}