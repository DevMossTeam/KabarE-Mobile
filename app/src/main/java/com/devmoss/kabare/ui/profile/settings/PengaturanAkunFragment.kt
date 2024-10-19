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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // Show the Switch Account Dialog when the icon is clicked
        binding.imgSwitchAccount.setOnClickListener {
            if (isAdded) {
                val switchAccountDialog = SwitchAccountDialog()
                switchAccountDialog.show(childFragmentManager, "SwitchAccountDialog")
            }
        }

        // Navigate to "Umum" when "Umum" is clicked
        binding.tvUmum.setOnClickListener {
                findNavController().navigate(R.id.action_pengaturanAkunFragment_to_umumFragment)

        }

        // Show the Logout Confirmation Dialog when the logout option is clicked
        binding.tvKeluar.setOnClickListener {
            if (isAdded) {
                val logoutDialog = KonfirmasiKeluarDialog(
                    onConfirm = {
                        performLogout()
                    },
                    onCancel = {
                        // Handle cancellation if needed (optional)
                    }
                )
                logoutDialog.show(childFragmentManager, "KonfirmasiKeluarDialog")
            }
        }
    }

    // Handle logout logic asynchronously
    private fun performLogout() {
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate clearing session, or handle actual logout logic
            // Add your logout logic here, such as clearing user data, token, etc.
            // Example: clearUserSession()

            // Return to the main thread to update UI
            withContext(Dispatchers.Main) {
                // Navigate to login screen or update UI after logout
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks by nullifying the binding when the view is destroyed
        _binding = null
    }
}
