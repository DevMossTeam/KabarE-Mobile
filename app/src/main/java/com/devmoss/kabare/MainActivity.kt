package com.devmoss.kabare

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
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

        window.statusBarColor = resources.getColor(R.color.background_white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar as the ActionBar
        setSupportActionBar(binding.toolbar)

        // Find the NavController
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Define AppBarConfiguration for fragments with no up button
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_cari,
                R.id.navigation_notifications,
                R.id.navigation_profil,
                R.id.navigation_manajemen_artikel
            )
        )

        // Set up ActionBar with NavController and the AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up BottomNavigationView with NavController
        binding.navView.setupWithNavController(navController)

        // Add listener to manage visibility of Toolbar, BottomNavigationView, and other UI elements based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.introFragment,
                R.id.welcomeFragment,
                R.id.detailArtikelFragment,
                R.id.konfirmasiPublikasiFragment,
                R.id.buatArtikelFragment,
                R.id.ResetPasswordFragment,
                R.id.signUpInputFragment -> { // Tambahkan SignUpInputFragment
                    // Hide Toolbar and BottomNavigationView for these fragments
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = android.view.View.GONE
                }

                R.id.navigation_home -> {
                    // Set up for the home screen
                    binding.toolbar.title = null
                    binding.toolbarLogo.visibility = android.view.View.VISIBLE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = BottomNavigationView.VISIBLE
                }

                R.id.navigation_cari -> {
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = BottomNavigationView.VISIBLE
                }

                R.id.searchResultsFragment -> {
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = BottomNavigationView.GONE
                }

                R.id.navigation_notifications -> {
                    // Set up for the notifications screen
                    binding.toolbar.title = "Notifikasi"
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = BottomNavigationView.VISIBLE
                }

                R.id.navigation_manajemen_artikel -> {
                    // Set up for the article management screen
                    binding.toolbar.title = "Post Artikel"
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.VISIBLE
                }

                R.id.navigation_profil -> {
                    binding.toolbar.title = ""
                    binding.toolbarLogo.visibility = android.view.View.GONE
//                    binding.toolbarSetting.visibility = android.view.View.VISIBLE
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = android.view.View.VISIBLE
                }

                R.id.signInFragment -> {
                    // Set up for the Sign In screen
                    binding.toolbar.title = "Sign In"
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }

                R.id.signUpFragment -> {
                    // Set up for the Sign Up screen
                    binding.toolbar.title = "Sign Up"
                    binding.toolbarLogo.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }

                R.id.settingsFragment,
                R.id.umumFragment,
                R.id.settingNotifikasiFragment,
                R.id.keamananFragment,
                R.id.pusatBantuanFragment,
                R.id.tentangFragment,
                R.id.licenseFragment,
                R.id.hubungiFragment -> {
                    // Set up for settings and related fragments
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }

                else -> {
                    // Default behavior for other fragments
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    // Ensure proper back navigation support
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}