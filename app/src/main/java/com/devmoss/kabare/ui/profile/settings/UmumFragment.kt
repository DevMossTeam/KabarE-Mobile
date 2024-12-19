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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.devmoss.kabare.R
import com.devmoss.kabare.ui.profile.settings.viewmodels.UmumViewModel
import com.devmoss.kabare.data.repository.UserRepository
import java.io.ByteArrayOutputStream

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

                // Update profile picture
                val encodedImage = encodeImage(it)
                updateProfilePic(encodedImage)
            }
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                imgProfile.setImageBitmap(it)

                // Update profile picture
                val encodedImage = encodeImage(it)
                updateProfilePic(encodedImage)
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
                        Glide.with(this)
                            .load(user.profile_pic)
                            .circleCrop()
                            .into(imgProfile)
                    } else {
                        try {
                            val imageBytes = Base64.decode(user.profile_pic, Base64.DEFAULT)
                            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            imgProfile.setImageBitmap(decodedImage)
                        } catch (e: Exception) {
                            Log.e("ProfileImage", "Error decoding Base64 image", e)
                            imgProfile.setImageResource(R.drawable.ic_akun)
                        }
                    }
                } else {
                    imgProfile.setImageResource(R.drawable.ic_akun)
                }
            }
            result.onFailure {
                Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun encodeImage(imageUri: android.net.Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        inputStream?.let {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (it.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
        }
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun updateProfilePic(encodedImage: String) {
        val userUid = getUserUid()
        if (!userUid.isNullOrEmpty()) {
            umumViewModel.updateUserData(userUid, tvNamaLengkap.text.toString(), tvUsername.text.toString(), encodedImage)
        }
    }

    private fun showWarningDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Perhatian!")
            .setMessage("Apakah Anda yakin ingin mengubah status?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak") { _, _ -> }
        alertDialogBuilder.create().show()
    }

    private fun showCameraGalleryOptions() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri")
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(galleryIntent)
    }

    private fun showEditNamaLengkap() {
        llNamaLengkapDisplay.visibility = View.GONE
        llNamaLengkapEdit.visibility = View.VISIBLE
        etNamaLengkap.setText(tvNamaLengkap.text)
    }

    private fun saveNamaLengkap() {
        val newNamaLengkap = etNamaLengkap.text.toString()
        tvNamaLengkap.text = newNamaLengkap
        llNamaLengkapDisplay.visibility = View.VISIBLE
        llNamaLengkapEdit.visibility = View.GONE
        // Call ViewModel to save new data
        updateUserData()
    }

    private fun cancelEditNamaLengkap() {
        llNamaLengkapDisplay.visibility = View.VISIBLE
        llNamaLengkapEdit.visibility = View.GONE
    }

    private fun updateUserData() {
        val userUid = getUserUid()
        if (userUid != null) {
            umumViewModel.updateUserData(userUid, tvNamaLengkap.text.toString(), tvUsername.text.toString(), "")
        }
    }

    private fun showEditUsername() {
        llUsernameDisplay.visibility = View.GONE
        llUsernameEdit.visibility = View.VISIBLE
        etUsername.setText(tvUsername.text)
    }

    private fun saveUsername() {
        val newUsername = etUsername.text.toString()
        tvUsername.text = newUsername
        llUsernameDisplay.visibility = View.VISIBLE
        llUsernameEdit.visibility = View.GONE
        // Call ViewModel to save new data
        updateUserData()
    }

    private fun cancelEditUsername() {
        llUsernameDisplay.visibility = View.VISIBLE
        llUsernameEdit.visibility = View.GONE
    }

    private fun getUserUid(): String? {
        val userRepository = UserRepository(requireContext())
        return userRepository.getUserUid()
    }
}