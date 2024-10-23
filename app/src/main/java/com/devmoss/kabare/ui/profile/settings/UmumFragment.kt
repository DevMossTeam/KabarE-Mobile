package com.devmoss.kabare.ui.profile.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R
import android.Manifest
import com.bumptech.glide.Glide

class UmumFragment : Fragment() {

    // Deklarasi variabel untuk Nama Lengkap
    private lateinit var tvNamaLengkap: TextView
    private lateinit var imgEditNamaLengkap: ImageView
    private lateinit var etNamaLengkap: EditText
    private lateinit var btnSimpanNamaLengkap: TextView
    private lateinit var btnBatalNamaLengkap: TextView
    private lateinit var llNamaLengkapDisplay: LinearLayout
    private lateinit var llNamaLengkapEdit: LinearLayout

    // Deklarasi variabel untuk Username
    private lateinit var tvUsername: TextView
    private lateinit var imgEditUsername: ImageView
    private lateinit var etUsername: EditText
    private lateinit var btnSimpanUsername: TextView
    private lateinit var btnBatalUsername: TextView
    private lateinit var llUsernameDisplay: LinearLayout
    private lateinit var llUsernameEdit: LinearLayout

    // Deklarasi variabel untuk Bio
    private lateinit var tvBio: TextView
    private lateinit var imgEditBio: ImageView
    private lateinit var etBio: EditText
    private lateinit var btnSimpanBio: TextView
    private lateinit var btnBatalBio: TextView
    private lateinit var llBioDisplay: LinearLayout
    private lateinit var llBioEdit: LinearLayout

    // Deklarasi variabel untuk Info
    private lateinit var tvInfo: TextView
    private lateinit var imgEditInfo: ImageView
    private lateinit var etInfo: EditText
    private lateinit var btnSimpanInfo: TextView
    private lateinit var btnBatalInfo: TextView
    private lateinit var llInfoDisplay: LinearLayout
    private lateinit var llInfoEdit: LinearLayout
    private lateinit var tvBatasKarakterInfo: TextView

    // Deklarasi variabel untuk Status
    private lateinit var tvStatus: TextView
    private lateinit var imgEditStatus: ImageView

    private lateinit var imgProfile: ImageView
    private lateinit var imgCamera: CardView
    private val PICK_IMAGE_REQUEST = 1
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val CAMERA_REQUEST_CODE = 2
    private val CAMERA_PERMISSION_CODE = 3
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        // Initialize Bio components
        tvBio = view.findViewById(R.id.tv_bio)
        imgEditBio = view.findViewById(R.id.img_edit_bio)
        etBio = view.findViewById(R.id.et_bio)
        btnSimpanBio = view.findViewById(R.id.btn_simpan_bio)
        btnBatalBio = view.findViewById(R.id.btn_batal_bio)
        llBioDisplay = view.findViewById(R.id.ll_bio_display)
        llBioEdit = view.findViewById(R.id.ll_bio_edit)

        // Initialize Info components
        tvInfo = view.findViewById(R.id.tv_info)
        imgEditInfo = view.findViewById(R.id.img_edit_identitas_saya)
        etInfo = view.findViewById(R.id.et_info)
        btnSimpanInfo = view.findViewById(R.id.btn_simpan_info)
        btnBatalInfo = view.findViewById(R.id.btn_batal_info)
        llInfoDisplay = view.findViewById(R.id.ll_info_display)
        llInfoEdit = view.findViewById(R.id.ll_info_edit)
        tvBatasKarakterInfo = view.findViewById(R.id.tv_batas_karakter_info)

        // Initialize Status components
        tvStatus = view.findViewById(R.id.tv_posisi)
        imgEditStatus = view.findViewById(R.id.img_edit_status)

        imgProfile = view.findViewById(R.id.img_profile)
        imgCamera = view.findViewById<CardView>(R.id.img_camera)

        imgCamera.setOnClickListener {
            showCameraGalleryOptions() // Call method to show options
        }

        imgEditNamaLengkap.setOnClickListener { showEditNamaLengkap() }
        btnSimpanNamaLengkap.setOnClickListener { saveNamaLengkap() }
        btnBatalNamaLengkap.setOnClickListener { cancelEditNamaLengkap() }

        imgEditUsername.setOnClickListener { showEditUsername() }
        btnSimpanUsername.setOnClickListener { saveUsername() }
        btnBatalUsername.setOnClickListener { cancelEditUsername() }

        imgEditBio.setOnClickListener { showEditBio() }
        btnSimpanBio.setOnClickListener { saveBio() }
        btnBatalBio.setOnClickListener { cancelEditBio() }

        imgEditInfo.setOnClickListener { showEditInfo() }
        btnSimpanInfo.setOnClickListener { saveInfo() }
        btnBatalInfo.setOnClickListener { cancelEditInfo() }

        imgEditStatus.setOnClickListener { showWarningDialog() }

        etInfo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvBatasKarakterInfo.text = "${s?.length ?: 0}/50"
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    // Metode untuk menampilkan dialog peringatan
    private fun showWarningDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Perhatian")
        alertDialogBuilder.setMessage("Silahkan menghubungi admin untuk mengubah informasi ini.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Metode untuk menampilkan dan menyembunyikan layout edit Nama Lengkap
    private fun showEditNamaLengkap() {
        llNamaLengkapDisplay.visibility = View.GONE
        llNamaLengkapEdit.visibility = View.VISIBLE
        etNamaLengkap.setText(tvNamaLengkap.text.toString())
    }

    private fun saveNamaLengkap() {
        val newName = etNamaLengkap.text.toString().trim()
        if (newName.isNotEmpty()) {
            tvNamaLengkap.text = newName
            Toast.makeText(context, "Nama lengkap berhasil diubah", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        llNamaLengkapEdit.visibility = View.GONE
        llNamaLengkapDisplay.visibility = View.VISIBLE
    }

    private fun cancelEditNamaLengkap() {
        llNamaLengkapEdit.visibility = View.GONE
        llNamaLengkapDisplay.visibility = View.VISIBLE
    }

    // Metode untuk menampilkan dan menyembunyikan layout edit Username
    private fun showEditUsername() {
        llUsernameDisplay.visibility = View.GONE
        llUsernameEdit.visibility = View.VISIBLE
        etUsername.setText(tvUsername.text.toString())
    }

    private fun saveUsername() {
        val newUsername = etUsername.text.toString().trim()
        if (newUsername.isNotEmpty()) {
            tvUsername.text = newUsername
            Toast.makeText(context, "Username berhasil diubah", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        llUsernameEdit.visibility = View.GONE
        llUsernameDisplay.visibility = View.VISIBLE
    }

    private fun cancelEditUsername() {
        llUsernameEdit.visibility = View.GONE
        llUsernameDisplay.visibility = View.VISIBLE
    }

    // Metode untuk menampilkan dan menyembunyikan layout edit Bio
    private fun showEditBio() {
        llBioDisplay.visibility = View.GONE
        llBioEdit.visibility = View.VISIBLE
        etBio.setText(tvBio.text.toString())
    }

    private fun saveBio() {
        val newBio = etBio.text.toString().trim()
        if (newBio.isNotEmpty()) {
            tvBio.text = newBio
            Toast.makeText(context, "Bio berhasil diubah", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Bio tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        llBioEdit.visibility = View.GONE
        llBioDisplay.visibility = View.VISIBLE
    }

    private fun cancelEditBio() {
        llBioEdit.visibility = View.GONE
        llBioDisplay.visibility = View.VISIBLE
    }

    // Metode untuk menampilkan dan menyembunyikan layout edit Info
    private fun showEditInfo() {
        llInfoDisplay.visibility = View.GONE
        llInfoEdit.visibility = View.VISIBLE
        etInfo.setText(tvInfo.text.toString())
    }

    private fun saveInfo() {
        val newInfo = etInfo.text.toString().trim()
        if (newInfo.isNotEmpty()) {
            tvInfo.text = newInfo
            Toast.makeText(context, "Info berhasil diubah", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Info tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        llInfoEdit.visibility = View.GONE
        llInfoDisplay.visibility = View.VISIBLE
    }

    private fun cancelEditInfo() {
        llInfoEdit.visibility = View.GONE
        llInfoDisplay.visibility = View.VISIBLE
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        } else {
            // Permission is already granted, open camera
            openCamera()
        }
    }

    private fun openCamera() {
        // Start camera intent here
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, open camera
                openCamera()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to take a picture.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showCameraGalleryOptions() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission() // Check permission before opening camera
                1 -> openGallery() // If Gallery is selected
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST_CODE -> {
                    // Get the image from the gallery
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        // Use Glide to load the selected image into imgProfile
                        Glide.with(requireContext())
                            .load(it)
                            .circleCrop() // Apply circular crop to the image
                            .into(imgProfile)
                    }
                }

                CAMERA_REQUEST_CODE -> {
                    // Get the photo taken by the camera
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        // Use Glide to load the captured photo into imgProfile
                        Glide.with(requireContext())
                            .load(it) // Convert Bitmap to URI for Glide
                            .circleCrop() // Apply circular crop to the image
                            .into(imgProfile)
                    }
                }
            }
        }
    }
}