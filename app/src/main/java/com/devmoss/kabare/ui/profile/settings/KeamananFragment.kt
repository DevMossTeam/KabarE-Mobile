package com.devmoss.kabare.ui.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.devmoss.kabare.R
import com.devmoss.kabare.data.repository.UserRepository
import com.devmoss.kabare.ui.auth.popups.ChangePasswordDialog
//import com.devmoss.kabare.ui.auth.popups.KonfirmasiUbahEmail
import com.devmoss.kabare.ui.auth.popups.LupaPasswordKonfirmasiEmailDialog
import com.devmoss.kabare.ui.profile.settings.viewmodels.KeamananViewModel
import com.google.firebase.auth.FirebaseAuth

class KeamananFragment : Fragment() {

    private lateinit var tvEmailDisplay: TextView
    private lateinit var etEmailEdit: EditText
    private lateinit var btnEditEmail: ImageView
    private lateinit var btnSaveEmail: TextView
    private lateinit var btnCancelEmail: TextView
    private lateinit var llEmailDisplay: LinearLayout
    private lateinit var llEmailEdit: LinearLayout
    private lateinit var llLupaKataSandi: LinearLayout
    private lateinit var llGantiPassword: LinearLayout

    private val viewModel: KeamananViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keamanan, container, false)

        // Initialize views
        tvEmailDisplay = view.findViewById(R.id.tv_email_display)
        etEmailEdit = view.findViewById(R.id.et_email)
        btnEditEmail = view.findViewById(R.id.img_edit_email)
        btnSaveEmail = view.findViewById(R.id.btn_simpan)
        btnCancelEmail = view.findViewById(R.id.btn_batal)
        llEmailDisplay = view.findViewById(R.id.ll_email_display)
        llEmailEdit = view.findViewById(R.id.ll_email_edit)
        llLupaKataSandi = view.findViewById(R.id.llLupaKataSandi)
        llGantiPassword = view.findViewById(R.id.llGantiPassword)

        // Fetch user data
        val userUid = getUserUid() ?: run {
            showToast(R.string.failed_to_fetch_user_data)
            return view
        }
        viewModel.fetchUserData(userUid)

        // Observe LiveData from ViewModel
        observeViewModel()

        // Set onClickListeners for buttons
        btnEditEmail.setOnClickListener { showEditEmail() }
        btnSaveEmail.setOnClickListener { handleSaveEmail() }
        btnCancelEmail.setOnClickListener { cancelEditEmail() }
        llLupaKataSandi.setOnClickListener { showForgotPasswordDialog() }
        llGantiPassword.setOnClickListener { showChangePasswordDialog() }

        return view
    }

    private fun observeViewModel() {
        // Observe user data
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                tvEmailDisplay.text = user.email
            }.onFailure { error ->
                showToast(R.string.failed_to_fetch_user_data)
                error.printStackTrace()
            }
        }

        // Observe email updates
        viewModel.email.observe(viewLifecycleOwner) { email ->
            tvEmailDisplay.text = email
        }

        // Observe email update status
        viewModel.emailUpdateStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
//                showToast(R.string.email_updated)
                cancelEditEmail()
            } else {
                showToast(R.string.email_invalid)
            }
        }

        // Observe password change status
        viewModel.passwordChangeStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                showToast(R.string.password_changed)
            } else {
                showToast(R.string.password_change_failed)
            }
        }
    }

    private fun showEditEmail() {
        llEmailDisplay.visibility = View.GONE
        llEmailEdit.visibility = View.VISIBLE
        etEmailEdit.setText(tvEmailDisplay.text.toString())
        etEmailEdit.requestFocus()
//        showChangeEmailDialog()
    }

    private fun handleSaveEmail() {
        val newEmail = etEmailEdit.text.toString().trim()

        if (newEmail.isEmpty()) {
            showToast(R.string.email_empty)
            return
        }

        val currentEmail = viewModel.email.value ?: ""
        if (newEmail == currentEmail) {
            showToast(R.string.email_same_as_current)
            return
        }

        val userUid = getUserUid() ?: run {
            showToast(R.string.failed_to_fetch_user_data)
            return
        }

        viewModel.updateEmail(userUid, newEmail)
    }

    private fun cancelEditEmail() {
        llEmailEdit.visibility = View.GONE
        llEmailDisplay.visibility = View.VISIBLE
        etEmailEdit.text.clear()
    }

    private fun showForgotPasswordDialog() {
        val lupaPasswordDialog = LupaPasswordKonfirmasiEmailDialog()
        lupaPasswordDialog.show(parentFragmentManager, "LupaPasswordKonfirmasiEmailDialog")
    }

    private fun showChangePasswordDialog() {
        // Inisialisasi UserRepository
        val userRepository = UserRepository(requireContext()) // Pastikan `requireContext()` sesuai dengan lokasi panggilan
        val changePasswordDialog = ChangePasswordDialog(userRepository)
        changePasswordDialog.show(parentFragmentManager, "ChangePasswordDialog")
    }

//    private fun showChangeEmailDialog() {
//        val changeEmailDialog = KonfirmasiUbahEmail()
//        changeEmailDialog.show(parentFragmentManager, "ChangeEmailDiaolog")
//    }

    private fun showToast(messageId: Int) {
        Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
    }

    private fun getUserUid(): String? {
        val userRepository = UserRepository(requireContext())
        return userRepository.getUserUid() ?: FirebaseAuth.getInstance().currentUser?.uid
    }
}