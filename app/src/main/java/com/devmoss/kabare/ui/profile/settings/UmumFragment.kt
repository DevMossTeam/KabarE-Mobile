package com.devmoss.kabare.ui.profile.settings

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.profile.settings.viewmodels.UmumViewModel
import com.devmoss.kabare.data.repository.UserRepository

class UmumFragment : Fragment() {

    private lateinit var tvNamaLengkap: TextView
    private lateinit var imgEditNamaLengkap: ImageView
    private lateinit var etNamaLengkap: EditText
    private lateinit var btnSimpanNamaLengkap: TextView
    private lateinit var btnBatalNamaLengkap: TextView
    private lateinit var llNamaLengkapDisplay: LinearLayout
    private lateinit var llNamaLengkapEdit: LinearLayout

    private lateinit var tvUsername: TextView
    private lateinit var imgEditUsername: ImageView
    private lateinit var etUsername: EditText
    private lateinit var btnSimpanUsername: TextView
    private lateinit var btnBatalUsername: TextView
    private lateinit var llUsernameDisplay: LinearLayout
    private lateinit var llUsernameEdit: LinearLayout

    private lateinit var tvStatus: TextView
    private lateinit var imgEditStatus: ImageView

    private lateinit var imgProfile: ImageView
    private lateinit var imgCamera: CardView
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 101
    private val CAMERA_PERMISSION_CODE = 3

    private val umumViewModel: UmumViewModel by viewModels()

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(imgProfile)
            }
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                imgProfile.setImageBitmap(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_umum, container, false)

        // Initialize components
        tvNamaLengkap = view.findViewById(R.id.tv_nama_lengkap)
        imgEditNamaLengkap = view.findViewById(R.id.img_edit_nama_lengkap)
        etNamaLengkap = view.findViewById(R.id.et_nama_lengkap)
        btnSimpanNamaLengkap = view.findViewById(R.id.btn_simpan)
        btnBatalNamaLengkap = view.findViewById(R.id.btn_batal)
        llNamaLengkapDisplay = view.findViewById(R.id.ll_nama_lengkap_display)
        llNamaLengkapEdit = view.findViewById(R.id.ll_nama_lengkap_edit)

        // Initialize Username components
        tvUsername = view.findViewById(R.id.tv_username)
        imgEditUsername = view.findViewById(R.id.img_edit_username)
        etUsername = view.findViewById(R.id.et_username)
        btnSimpanUsername = view.findViewById(R.id.btn_simpan_username)
        btnBatalUsername = view.findViewById(R.id.btn_batal_username)
        llUsernameDisplay = view.findViewById(R.id.ll_username_display)
        llUsernameEdit = view.findViewById(R.id.ll_username_edit)

        // Initialize Status components
        tvStatus = view.findViewById(R.id.tv_posisi)
        imgEditStatus = view.findViewById(R.id.img_edit_status)

        imgProfile = view.findViewById(R.id.img_profile)
        imgCamera = view.findViewById<CardView>(R.id.img_camera)

        // Set listeners for actions
        imgCamera.setOnClickListener {
            showCameraGalleryOptions() // Show camera/gallery options
        }

        imgEditNamaLengkap.setOnClickListener { showEditNamaLengkap() }
        btnSimpanNamaLengkap.setOnClickListener { saveNamaLengkap() }
        btnBatalNamaLengkap.setOnClickListener { cancelEditNamaLengkap() }

        imgEditUsername.setOnClickListener { showEditUsername() }
        btnSimpanUsername.setOnClickListener { saveUsername() }
        btnBatalUsername.setOnClickListener { cancelEditUsername() }

        imgEditStatus.setOnClickListener { showWarningDialog() }

        // Fetch UID from UserRepository
        val userRepository = UserRepository(requireContext())
        val userUid = userRepository.getUserUid()

        if (!userUid.isNullOrEmpty()) {
            // If UID is not null or empty, fetch user data
            umumViewModel.fetchUserData(userUid)
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        umumViewModel.userData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { user ->
                // Set user data to TextViews and EditText
                tvNamaLengkap.text = user.nama_lengkap
                tvUsername.text = user.nama_pengguna
                tvStatus.text = user.role

                // Load profile picture
                if (!user.profile_pic.isNullOrEmpty()) {
                    if (user.profile_pic.startsWith("http")) {
                        // If the profile picture is a URL, load it with Glide
                        Glide.with(this)
                            .load(user.profile_pic)  // Load image from URL
                            .circleCrop()  // Apply circle crop to image
                            .into(imgProfile)
                    } else {
                        try {
                            // If the profile picture is a Base64 string, decode and set it
                            val imageBytes = Base64.decode(user.profile_pic, Base64.DEFAULT)
                            val decodedImage =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            imgProfile.setImageBitmap(decodedImage)
                        } catch (e: Exception) {
                            // In case Base64 decoding fails, log the error and set a default image
                            Log.e("ProfileImage", "Error decoding Base64 image", e)
                            imgProfile.setImageResource(R.drawable.ic_akun) // Default profile image
                        }
                    }
                } else {
                    imgProfile.setImageResource(R.drawable.ic_akun)
                }
            }
            result.onFailure {
                // In case of failure, show a toast message
                Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    // Method to show warning dialog
    private fun showWarningDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Perhatian")
        alertDialogBuilder.setMessage("Silahkan menghubungi admin untuk mengubah informasi ini.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Method to show and hide the layout for editing Nama Lengkap
    private fun showEditNamaLengkap() {
        llNamaLengkapDisplay.visibility = View.GONE
        llNamaLengkapEdit.visibility = View.VISIBLE
        etNamaLengkap.setText(tvNamaLengkap.text.toString())
    }

    private fun saveNamaLengkap() {
        val newName = etNamaLengkap.text.toString().trim()

        if (newName.isEmpty() || newName == tvNamaLengkap.text.toString()) {
            Toast.makeText(context, "Nama lengkap tidak valid atau sama seperti sebelumnya", Toast.LENGTH_SHORT).show()
            return
        }

        val userUid = getUserUid()
        if (!userUid.isNullOrEmpty()) {
            // Now, we pass the new full name and the existing username (tvUsername.text.toString())
            umumViewModel.updateUserData(userUid, newName, tvUsername.text.toString())
            umumViewModel.updateResult.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    tvNamaLengkap.text = newName
                    Toast.makeText(context, "Nama lengkap berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    llNamaLengkapEdit.visibility = View.GONE
                    llNamaLengkapDisplay.visibility = View.VISIBLE
                }
                result.onFailure {
                    Toast.makeText(context, "Gagal memperbarui nama lengkap", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cancelEditNamaLengkap() {
        llNamaLengkapEdit.visibility = View.GONE
        llNamaLengkapDisplay.visibility = View.VISIBLE
    }

    // Method to show and hide the layout for editing Username
    private fun showEditUsername() {
        llUsernameDisplay.visibility = View.GONE
        llUsernameEdit.visibility = View.VISIBLE
        etUsername.setText(tvUsername.text.toString())
    }

    private fun saveUsername() {
        val newUsername = etUsername.text.toString().trim()
        if (newUsername.isEmpty() || newUsername == tvUsername.text.toString()) {
            Toast.makeText(context, "Username tidak valid atau sama seperti sebelumnya", Toast.LENGTH_SHORT).show()
            return
        }

        val newName = tvNamaLengkap.text.toString() // Ensure we're passing the current full name as well
        val userUid = getUserUid()
        if (!userUid.isNullOrEmpty()) {
            // Now pass both newName and newUsername
            umumViewModel.updateUserData(userUid, newName, newUsername)
            umumViewModel.updateResult.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    tvUsername.text = newUsername // Update the displayed username
                    Toast.makeText(context, "Username berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    llUsernameEdit.visibility = View.GONE
                    llUsernameDisplay.visibility = View.VISIBLE
                }
                result.onFailure {
                    Toast.makeText(context, "Gagal memperbarui username", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cancelEditUsername() {
        llUsernameEdit.visibility = View.GONE
        llUsernameDisplay.visibility = View.VISIBLE
    }

    private fun getUserUid(): String? {
        // Retrieve UID from repository
        val userRepository = UserRepository(requireContext())
        return userRepository.getUserUid()
    }


    // Methods for Camera and Gallery options
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    private fun showCameraGalleryOptions() {
        val options = arrayOf("Open Camera", "Open Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> openGallery()
            }
        }
        builder.show()
    }
}