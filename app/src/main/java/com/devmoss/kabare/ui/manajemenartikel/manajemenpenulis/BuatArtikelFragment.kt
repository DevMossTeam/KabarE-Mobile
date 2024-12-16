package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import com.devmoss.kabare.databinding.FragmentBuatArtikelBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BuatArtikelFragment : Fragment() {

    private var _binding: FragmentBuatArtikelBinding? = null
    private val binding get() = _binding!!

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private val CAMERA_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuatArtikelBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener{
            requireActivity().onBackPressed() // Kembali ke fragment sebelumnya
        }

        binding.btnMenu.setOnClickListener {
            showPopupMenu(binding.btnMenu)
        }

        binding.formatFont.setOnClickListener {
            if(binding.fontOptions.visibility == View.GONE){
                binding.fontOptions.visibility = View.VISIBLE
            }else{
                binding.fontOptions.visibility = View.GONE
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            showImagePickerMenu()
        }

        // Implementasi fungsi untuk setiap tombol
        binding.btnBold.setOnClickListener {
            applyStyleToSelection(Typeface.BOLD)
        }

        binding.btnItalic.setOnClickListener {
            applyStyleToSelection(Typeface.ITALIC)
        }

        binding.btnNumbering.setOnClickListener {
            insertNumbering()
        }

        binding.btnListBulleted.setOnClickListener {
            insertBulletList()
        }

        binding.btnLink.setOnClickListener {
            Toast.makeText(requireContext(), "Insert link clicked", Toast.LENGTH_SHORT).show()
            // Implementasikan fungsi untuk menambahkan link di sini
        }

        binding.btnTag.setOnClickListener {
            Toast.makeText(requireContext(), "Insert tag clicked", Toast.LENGTH_SHORT).show()
            // Implementasikan fungsi untuk menambahkan tag di sini
        }

        binding.btnQuote.setOnClickListener {
            applyQuoteStyle()
        }

        binding.btnBaselineCode.setOnClickListener {
            Toast.makeText(requireContext(), "Insert code block clicked", Toast.LENGTH_SHORT).show()
            // Implementasikan fungsi untuk menambahkan kode di sini
        }

        binding.btnArrowBack.setOnClickListener {
            // Fungsi Undo (memerlukan stack aksi sebelumnya)
            Toast.makeText(requireContext(), "Undo clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnArrowForward.setOnClickListener {
            // Fungsi Redo (memerlukan stack aksi sebelumnya)
            Toast.makeText(requireContext(), "Redo clicked", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // Tampilkan menu untuk memilih dari galeri atau kamera
    private fun showImagePickerMenu() {
        val popupMenu = android.widget.PopupMenu(requireContext(), binding.btnAddPhoto)
        popupMenu.menuInflater.inflate(R.menu.menu_image_picker, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_take_photo -> {
                    checkCameraPermission()
                    true
                }
                R.id.menu_choose_from_gallery -> {
                    openGallery()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = android.widget.PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_buat_artikel, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_draft -> {
                    Toast.makeText(requireContext(), "Simpan ke Draf", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_preview -> {
                    Toast.makeText(requireContext(), "Preview", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_publish -> {
                    findNavController().navigate(R.id.action_buatArtikelFragment_to_konfirmasiPublikasiFragment)
                    Toast.makeText(requireContext(), "Publish", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // Cek izin kamera
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    // Buka kamera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    // Buka galeri
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    // Penanganan hasil izin kamera
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Izin kamera diperlukan untuk mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Penanganan hasil kamera atau galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivArticleImage.setImageBitmap(photo)
                    saveImageToInternalStorage(photo)
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri: Uri? = data?.data
                    binding.ivArticleImage.setImageURI(selectedImageUri)
                }
            }
        }
    }

    // Simpan gambar ke penyimpanan internal
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val filename = "article_image_${System.currentTimeMillis()}.jpg"
        var fileOutputStream: FileOutputStream? = null
        var fileUri: Uri? = null

        try {
            val file = File(requireContext().getDir("images", Context.MODE_PRIVATE), filename)
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileUri = Uri.fromFile(file)
            Toast.makeText(requireContext(), "Gambar disimpan", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return fileUri
    }

    // Fungsi untuk menerapkan bold atau italic pada teks yang dipilih
    private fun applyStyleToSelection(style: Int) {
        val start = binding.etContent.selectionStart
        val end = binding.etContent.selectionEnd

        if (start < end) {
            val spannable = binding.etContent.text as Spannable
            spannable.setSpan(
                StyleSpan(style),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    // Fungsi untuk menambahkan numbering pada teks yang dipilih
    private fun insertNumbering() {
        val currentText = binding.etContent.text.toString()
        val numberedText = addNumberingToText(currentText)
        binding.etContent.setText(numberedText)
    }

    private fun addNumberingToText(text: String): String {
        val lines = text.split("\n")
        val numberedLines = lines.mapIndexed { index, line -> "${index + 1}. $line" }
        return TextUtils.join("\n", numberedLines)
    }

    // Fungsi untuk menambahkan bullet list pada teks yang dipilih
    private fun insertBulletList() {
        val start = binding.etContent.selectionStart
        val end = binding.etContent.selectionEnd

        if (start < end) {
            val spannable = binding.etContent.text as Spannable
            spannable.setSpan(
                BulletSpan(15), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    // Fungsi untuk menambahkan style quote pada teks
    private fun applyQuoteStyle() {
        val start = binding.etContent.selectionStart
        val end = binding.etContent.selectionEnd

        if (start < end) {
            val spannable = binding.etContent.text as SpannableStringBuilder
            val color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            spannable.setSpan(
                ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
