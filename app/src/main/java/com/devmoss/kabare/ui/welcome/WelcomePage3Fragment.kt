package com.devmoss.kabare.ui.welcome

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R

class WelcomePage3Fragment : Fragment() {

    private lateinit var imageView: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var currentImageIndex = 0

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    // Daftar aktivitas untuk permintaan izin
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        handlePermissionResult(isGranted)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.vec3)

        // Periksa pengaturan default
        checkAndSetDefaultPreferences()

        // Periksa dan minta izin notifikasi jika diperlukan
        checkAndRequestNotificationPermission()

        // Mulai animasi gambar
        startImageSwitching()
    }

    private fun checkAndSetDefaultPreferences() {
        val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)
        if (isFirstRun) {
            // Atur pengaturan default
            sharedPreferences.edit()
                .putBoolean("notifications_enabled", true)
                .putBoolean("popular_news", true)
                .putBoolean("latest_news", true)
                .apply()

            // Tandai bahwa aplikasi sudah pernah dijalankan
            sharedPreferences.edit().putBoolean("is_first_run", false).apply()

            Toast.makeText(requireContext(), "Pengaturan default diaktifkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Minta izin
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Jika izin sudah diberikan
                handlePermissionResult(true)
            }
        } else {
            // Pada versi Android di bawah TIRAMISU, izinkan secara default
            handlePermissionResult(true)
        }
    }

    // Tangani hasil izin
    private fun handlePermissionResult(isGranted: Boolean) {
        if (isGranted) {
            Toast.makeText(requireContext(), "Notifikasi diizinkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Notifikasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startImageSwitching() {
        val images = arrayOf(R.drawable.vec3, R.drawable.vec3_2, R.drawable.vec3_3)

        val runnable = object : Runnable {
            override fun run() {
                // Ganti gambar sesuai dengan indeks saat ini
                imageView.setImageResource(images[currentImageIndex])
                // Update indeks untuk gambar berikutnya
                currentImageIndex = (currentImageIndex + 1) % images.size

                // Ulangi perubahan gambar setiap 0.5 detik (500 ms)
                handler.postDelayed(this, 500)
            }
        }
        // Jalankan animasi pertama kali
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Hentikan handler untuk menghindari memory leaks
    }
}