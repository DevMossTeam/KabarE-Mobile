package com.devmoss.kabare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devmoss.kabare.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Konfigurasi AppBar untuk halaman tertentu
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_cari, R.id.navigation_notifications, R.id.navigation_profil)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Tambahkan listener untuk NavController untuk mengatur visibilitas Toolbar dan BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Fragment yang menampilkan BottomNavigationView dan Toolbar
                R.id.navigation_home, R.id.navigation_cari, R.id.navigation_notifications, R.id.navigation_profil -> {
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = BottomNavigationView.VISIBLE
                }
                // Fragment yang menyembunyikan BottomNavigationView dan Toolbar
                R.id.introFragment, R.id.welcomeFragment -> {
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = BottomNavigationView.GONE
                }
                else -> {
                    // Fragment default, tampilkan Toolbar tapi sembunyikan BottomNavigationView jika tidak perlu
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
