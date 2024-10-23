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

class WelcomePage1Fragment : Fragment() {

    private lateinit var imageView: ImageView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.vec1)

        // Mulai animasi perubahan gambar
        startImageSwitching()
    }

    private fun startImageSwitching() {
        val runnable = object : Runnable {
            var isOriginalImage = true

            override fun run() {
                // Ganti gambar antara vec1 dan vec1.1
                if (isOriginalImage) {
                    imageView.setImageResource(R.drawable.vec1_1)
                } else {
                    imageView.setImageResource(R.drawable.vec1)
                }
                isOriginalImage = !isOriginalImage

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