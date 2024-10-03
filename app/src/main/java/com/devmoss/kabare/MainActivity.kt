package com.devmoss.kabare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.devmoss.kabare.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set your main layout

        // Only navigate to IntroFragment if savedInstanceState is null
        if (savedInstanceState == null) {
            // Assuming you have a NavHostFragment in your layout
            findNavController(R.id.nav_host_fragment).navigate(R.id.introFragment)
        }
    }
}