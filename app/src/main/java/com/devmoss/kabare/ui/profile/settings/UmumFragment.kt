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

    // Social Media Variables
    private lateinit var llAddSocialMedia1: LinearLayout
    private lateinit var llAddSocialMedia2: LinearLayout
    private lateinit var llAddSocialMedia3: LinearLayout
    private lateinit var llAddSocialMedia4: LinearLayout
    private lateinit var llAddSocialMedia5: LinearLayout

    private lateinit var btnSimpanSocialMedia1: TextView
    private lateinit var btnBatalSocialMedia1: TextView
    private lateinit var btnSimpanSocialMedia2: TextView
    private lateinit var btnBatalSocialMedia2: TextView
    private lateinit var btnSimpanSocialMedia3: TextView
    private lateinit var btnBatalSocialMedia3: TextView
    private lateinit var btnSimpanSocialMedia4: TextView
    private lateinit var btnBatalSocialMedia4: TextView
    private lateinit var btnSimpanSocialMedia5: TextView
    private lateinit var btnBatalSocialMedia5: TextView

    private lateinit var btnAddSocialMedia: Button
    private val socialMediaLinks = mutableListOf<String?>(null, null, null, null, null)
    private var linkCount = 0

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

        // Initialize social media components
        llAddSocialMedia1 = view.findViewById(R.id.ll_add_social_media1)
        llAddSocialMedia2 = view.findViewById(R.id.ll_add_social_media2)
        llAddSocialMedia3 = view.findViewById(R.id.ll_add_social_media3)
        llAddSocialMedia4 = view.findViewById(R.id.ll_add_social_media4)
        llAddSocialMedia5 = view.findViewById(R.id.ll_add_social_media5)
        btnAddSocialMedia = view.findViewById(R.id.btn_add_social_media)

        // Initialize components for save and cancel buttons for social media
        btnSimpanSocialMedia1 = view.findViewById(R.id.btn_simpan_social_media1)
        btnBatalSocialMedia1 = view.findViewById(R.id.btn_batal_social_media1)
        btnSimpanSocialMedia2 = view.findViewById(R.id.btn_simpan_social_media2)
        btnBatalSocialMedia2 = view.findViewById(R.id.btn_batal_social_media2)
        btnSimpanSocialMedia3 = view.findViewById(R.id.btn_simpan_social_media3)
        btnBatalSocialMedia3 = view.findViewById(R.id.btn_batal_social_media3)
        btnSimpanSocialMedia4 = view.findViewById(R.id.btn_simpan_social_media4)
        btnBatalSocialMedia4 = view.findViewById(R.id.btn_batal_social_media4)
        btnSimpanSocialMedia5 = view.findViewById(R.id.btn_simpan_social_media5)
        btnBatalSocialMedia5 = view.findViewById(R.id.btn_batal_social_media5)

        imgProfile = view.findViewById(R.id.img_profile)
        imgCamera = view.findViewById<CardView>(R.id.img_camera)

        imgCamera.setOnClickListener {
            showCameraGalleryOptions() // Call method to show options
        }

        // Set up click listeners
        btnSimpanSocialMedia1.setOnClickListener { saveSocialMediaLink(1) }
        btnBatalSocialMedia1.setOnClickListener { cancelSocialMediaLink(1) }
        btnSimpanSocialMedia2.setOnClickListener { saveSocialMediaLink(2) }
        btnBatalSocialMedia2.setOnClickListener { cancelSocialMediaLink(2) }
        btnSimpanSocialMedia3.setOnClickListener { saveSocialMediaLink(3) }
        btnBatalSocialMedia3.setOnClickListener { cancelSocialMediaLink(3) }
        btnSimpanSocialMedia4.setOnClickListener { saveSocialMediaLink(4) }
        btnBatalSocialMedia4.setOnClickListener { cancelSocialMediaLink(4) }
        btnSimpanSocialMedia5.setOnClickListener { saveSocialMediaLink(5) }
        btnBatalSocialMedia5.setOnClickListener { cancelSocialMediaLink(5) }

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

        btnAddSocialMedia.setOnClickListener {
            showNextSocialMediaLayout()
        }

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

    private fun showNextSocialMediaLayout() {
        // Check if the previous link (if any) is filled
        if (linkCount > 0 && !isSocialMediaLinkFilled(linkCount - 1)) {
            Toast.makeText(
                requireContext(),
                "Harap isi link sosial media sebelumnya sebelum melanjutkan.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Show the next layout if there are still available links
        if (linkCount < socialMediaLinks.size) {
            val layout = when (linkCount) {
                0 -> llAddSocialMedia1
                1 -> llAddSocialMedia2
                2 -> llAddSocialMedia3
                3 -> llAddSocialMedia4
                4 -> llAddSocialMedia5
                else -> return
            }

            // Show the layout
            layout.visibility = View.VISIBLE

            // Show separator and buttons
            layout.findViewById<View>(
                when (linkCount) {
                    0 -> R.id.separator1
                    1 -> R.id.separator2
                    2 -> R.id.separator3
                    3 -> R.id.separator4
                    4 -> R.id.separator5
                    else -> return // Should never reach here
                }
            ).visibility = View.VISIBLE

            layout.findViewById<TextView>(
                when (linkCount) {
                    0 -> R.id.btn_batal_social_media1
                    1 -> R.id.btn_batal_social_media2
                    2 -> R.id.btn_batal_social_media3
                    3 -> R.id.btn_batal_social_media4
                    4 -> R.id.btn_batal_social_media5
                    else -> return // Should never reach here
                }
            ).visibility = View.VISIBLE

            layout.findViewById<TextView>(
                when (linkCount) {
                    0 -> R.id.btn_simpan_social_media1
                    1 -> R.id.btn_simpan_social_media2
                    2 -> R.id.btn_simpan_social_media3
                    3 -> R.id.btn_simpan_social_media4
                    4 -> R.id.btn_simpan_social_media5
                    else -> return // Should never reach here
                }
            ).visibility = View.VISIBLE

            linkCount++ // Increment link count

            // Hide the add button if all social media fields are filled
            if (linkCount >= socialMediaLinks.size) {
                btnAddSocialMedia.visibility = View.GONE
            }
        }
    }

    // Function to check if the social media TextView at the given index is filled
    private fun isSocialMediaLinkFilled(index: Int): Boolean {
        val layout = when (index) {
            0 -> llAddSocialMedia1
            1 -> llAddSocialMedia2
            2 -> llAddSocialMedia3
            3 -> llAddSocialMedia4
            4 -> llAddSocialMedia5
            else -> return false // Invalid index
        }

        // Get the TextView for the current index
        val textViewId = when (index) {
            0 -> R.id.tv_social_media1
            1 -> R.id.tv_social_media2
            2 -> R.id.tv_social_media3
            3 -> R.id.tv_social_media4
            4 -> R.id.tv_social_media5
            else -> return false // Should never reach here
        }

        val textView = layout.findViewById<TextView>(textViewId)

        // Return true if the TextView is visible, otherwise false
        return textView.visibility == View.VISIBLE
    }


    private fun logChildViews(layout: ViewGroup) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            Log.d("UmumFragment", "Child view $i: $child with ID: ${child.id}")
        }
    }

    private fun saveSocialMediaLink(index: Int) {
        // Get the layout for the given index
        val layout = when (index) {
            1 -> llAddSocialMedia1
            2 -> llAddSocialMedia2
            3 -> llAddSocialMedia3
            4 -> llAddSocialMedia4
            5 -> llAddSocialMedia5
            else -> {
                Log.e("UmumFragment", "Invalid index: $index")
                return
            }
        }

        // Log all child views of the layout for debugging
        logChildViews(layout)

        // Define a map for social media EditText and TextView IDs
        val socialMediaEditTextIds = mapOf(
            1 to R.id.et_add_social_media1,
            2 to R.id.et_add_social_media2,
            3 to R.id.et_add_social_media3,
            4 to R.id.et_add_social_media4,
            5 to R.id.et_add_social_media5
        )

        val socialMediaTextViewIds = mapOf(
            1 to R.id.tv_social_media1,
            2 to R.id.tv_social_media2,
            3 to R.id.tv_social_media3,
            4 to R.id.tv_social_media4,
            5 to R.id.tv_social_media5
        )

        // Define a map for the separator line and buttons
        val separatorIds = mapOf(
            1 to R.id.separator1,
            2 to R.id.separator2,
            3 to R.id.separator3,
            4 to R.id.separator4,
            5 to R.id.separator5
        )

        val buttonIds = mapOf(
            1 to Pair(R.id.btn_batal_social_media1, R.id.btn_simpan_social_media1),
            2 to Pair(R.id.btn_batal_social_media2, R.id.btn_simpan_social_media2),
            3 to Pair(R.id.btn_batal_social_media3, R.id.btn_simpan_social_media3),
            4 to Pair(R.id.btn_batal_social_media4, R.id.btn_simpan_social_media4),
            5 to Pair(R.id.btn_batal_social_media5, R.id.btn_simpan_social_media5)
        )

        // Retrieve the EditText and TextView IDs from the map
        val editTextId = socialMediaEditTextIds[index]
        val textViewId = socialMediaTextViewIds[index]
        val separatorId = separatorIds[index] ?: return
        val buttonIdPair = buttonIds[index] ?: return

        // If either ID is null, log an error and return
        if (editTextId == null || textViewId == null) {
            Log.e("UmumFragment", "EditText or TextView ID is null for index: $index")
            return
        }

        // Find the EditText, TextView, separator, and buttons
        val editText = layout.findViewById<EditText>(editTextId) ?: run {
            Log.e("UmumFragment", "EditText is null for index: $index")
            return
        }
        val textView = layout.findViewById<TextView>(textViewId) ?: run {
            Log.e("UmumFragment", "TextView is null for index: $index")
            return
        }
        val separator = layout.findViewById<View>(separatorId) ?: run {
            Log.e("UmumFragment", "Separator is null for index: $index")
            return
        }

        // Unpack button IDs
        val (btnBatalId, btnSimpanId) = buttonIdPair

        // Find buttons
        val btnBatal = layout.findViewById<TextView>(btnBatalId) ?: run {
            Log.e("UmumFragment", "Cancel button is null for index: $index")
            return
        }
        val btnSimpan = layout.findViewById<TextView>(btnSimpanId) ?: run {
            Log.e("UmumFragment", "Save button is null for index: $index")
            return
        }

        // Log the retrieved views for debugging
        Log.d("UmumFragment", "EditText: $editText, TextView: $textView for index: $index")

        // Get the link from EditText
        val link = editText.text.toString().trim()

        // Validate the URL
        if (link.isNotEmpty() && isValidUrl(link)) {
            // Set the text in the corresponding TextView and make it visible
            textView.text = link
            textView.visibility = View.VISIBLE

            // Hide the EditText, separator, and buttons after saving
            editText.visibility = View.GONE
            separator.visibility = View.GONE
            btnBatal.visibility = View.GONE
            btnSimpan.visibility = View.GONE

            // Clear the EditText for future use (optional)
            editText.text.clear()

            Toast.makeText(
                requireContext(),
                "Link social media berhasil disimpan",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Hide the TextView if the link is empty or invalid
            textView.visibility = View.GONE

            // Optionally show the EditText again if needed
            editText.visibility = View.VISIBLE
            separator.visibility = View.VISIBLE
            btnBatal.visibility = View.VISIBLE
            btnSimpan.visibility = View.VISIBLE

            // Provide appropriate feedback
            if (link.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Link social media tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Link social media tidak valid. Pastikan formatnya benar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Function to validate URL
    private fun isValidUrl(url: String): Boolean {
        // Updated regex pattern for a more comprehensive URL validation
        val regex = Regex(
            """^(https?:\/\/)?                # Protocol (optional)
          |(www\.)?                        # www (optional)
          |([a-zA-Z0-9\-]+(\.[a-zA-Z]{2,})+) # Domain name
          |(:[0-9]{1,5})?                 # Optional port
          |(\/[^\s]*)?$""".trimMargin(), RegexOption.COMMENTS // End of the URL path
        )

        return regex.matches(url)
    }

    private fun cancelSocialMediaLink(index: Int) {
        val layout = when (index) {
            1 -> llAddSocialMedia1
            2 -> llAddSocialMedia2
            3 -> llAddSocialMedia3
            4 -> llAddSocialMedia4
            5 -> llAddSocialMedia5
            else -> return
        }

        // Reset the EditText and related views
        val editText = layout.findViewById<EditText>(
            when (index) {
                1 -> R.id.et_add_social_media1
                2 -> R.id.et_add_social_media2
                3 -> R.id.et_add_social_media3
                4 -> R.id.et_add_social_media4
                5 -> R.id.et_add_social_media5
                else -> return // Should never reach here
            }
        )

        // Clear the text field
        editText.text.clear()

        // Hide the layout and all related views
        layout.visibility = View.GONE

        // Hide the separator and buttons
        layout.findViewById<View>(
            when (index) {
                1 -> R.id.separator1
                2 -> R.id.separator2
                3 -> R.id.separator3
                4 -> R.id.separator4
                5 -> R.id.separator5
                else -> return // Should never reach here
            }
        ).visibility = View.GONE

        layout.findViewById<TextView>(
            when (index) {
                1 -> R.id.btn_batal_social_media1
                2 -> R.id.btn_batal_social_media2
                3 -> R.id.btn_batal_social_media3
                4 -> R.id.btn_batal_social_media4
                5 -> R.id.btn_batal_social_media5
                else -> return // Should never reach here
            }
        ).visibility = View.GONE

        layout.findViewById<TextView>(
            when (index) {
                1 -> R.id.btn_simpan_social_media1
                2 -> R.id.btn_simpan_social_media2
                3 -> R.id.btn_simpan_social_media3
                4 -> R.id.btn_simpan_social_media4
                5 -> R.id.btn_simpan_social_media5
                else -> return // Should never reach here
            }
        ).visibility = View.GONE

        // Reset the linkCount if we're canceling an active link
        if (linkCount > 0) {
            linkCount--
        }
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            // Permission already granted, proceed with camera action
            openCamera()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, open camera
                openCamera()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
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
                        // Set the selected image to the profile ImageView
                        val imgProfile = view?.findViewById<ImageView>(R.id.img_profile)
                        imgProfile?.setImageURI(it)
                    }
                }

                CAMERA_REQUEST_CODE -> {
                    // Get the photo taken by the camera
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        // Set the taken photo to the profile ImageView
                        val imgProfile = view?.findViewById<ImageView>(R.id.img_profile)
                        imgProfile?.setImageBitmap(it)
                    }
                }
            }
        }
    }
}