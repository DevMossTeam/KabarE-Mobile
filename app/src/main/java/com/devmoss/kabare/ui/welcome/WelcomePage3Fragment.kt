package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.devmoss.kabare.R

class WelcomePage3Fragment : Fragment() {

    private lateinit var imageView: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var currentImageIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.vec3)

        // Mulai animasi pergantian gambar
        startImageSwitching()
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
        // Hentikan handler untuk menghindari memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}