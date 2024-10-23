package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentPengaturanAkunBinding
import com.devmoss.kabare.ui.auth.popups.KonfirmasiKeluarDialog
import com.devmoss.kabare.ui.auth.popups.SwitchAccountDialog

class PengaturanAkunFragment : Fragment() {

    private var _binding: FragmentPengaturanAkunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPengaturanAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show the Switch Account Dialog when the edit icon is clicked
        binding.imgSwitch.setOnClickListener { // Update the ID to match your layout
            if (isAdded) {
                val switchAccountDialog = SwitchAccountDialog()
                switchAccountDialog.show(childFragmentManager, "SwitchAccountDialog")
            }
        }

        // Navigate to "Umum" when "tv_umum" is clicked
        binding.tvUmum.setOnClickListener { // Ensure this ID matches your layout
            findNavController().navigate(R.id.action_pengaturanAkunFragment_to_umumFragment)
        }

        // Navigate to "Notifikasi" when "tv_notifikasi" is clicked
        binding.tvNotifikasi.setOnClickListener { // Ensure this ID matches your layout
            findNavController().navigate(R.id.action_pengaturanAkunFragment_to_settingNotifikasiFragment)
        }

        // Navigate to "Keamanan" when "tv_keamanan" is clicked
        binding.tvKeamanan.setOnClickListener { // Ensure this ID matches your layout
            findNavController().navigate(R.id.action_pengaturanAkunFragment_to_keamananFragment)
        }

        binding.tvBantuan.setOnClickListener { // Ensure this ID matches your layout
            findNavController().navigate(R.id.action_pengaturanAkunFragment_to_pusatBantuanFragment)
        }

        binding.tvTentang.setOnClickListener { // Ensure this ID matches your layout
            findNavController().navigate(R.id.action_pengaturanAkunFragment_to_tentangFragment)
        }

        // Show the Logout Confirmation Dialog when the logout button is clicked
        binding.btnLogout.setOnClickListener { // Ensure this ID matches your layout
            if (isAdded) {
                val logoutDialog = KonfirmasiKeluarDialog(
                    onConfirm = {
                        // Perform the logout action here
                        performLogout() // Uncomment and implement the logout functionality
                    },
                    onCancel = {
                        // Handle cancellation if needed (optional)
                    }
                )
                logoutDialog.show(childFragmentManager, "KonfirmasiKeluarDialog")
            }
        }
    }

    private fun performLogout() {
        // Implement your logout logic here (e.g., clear user session, navigate to login screen)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks by nullifying the binding when the view is destroyed
        _binding = null
    }
}