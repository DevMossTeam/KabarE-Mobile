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

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar as the ActionBar
        setSupportActionBar(binding.toolbar)

        // Find the NavController
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Define AppBarConfiguration for fragments with no up button
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_cari, R.id.navigation_notifications, R.id.navigation_profil)
        )

        // Set up ActionBar with NavController and the AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up BottomNavigationView with NavController
        binding.navView.setupWithNavController(navController)

        // Add listener to manage visibility of Toolbar and BottomNavigationView based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_cari, R.id.navigation_artikel, R.id.navigation_notifications -> {
                    // Show the Toolbar and BottomNavigationView for these fragments
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)  // Disable back button
                }
                R.id.navigation_profil -> {
                    // Hide Toolbar and BottomNavigationView for profil screen
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = android.view.View.VISIBLE
                }
                R.id.introFragment, R.id.signInFragment, R.id.signUpFragment, R.id.welcomeFragment -> {
                    // Hide Toolbar and BottomNavigationView for intro, sign-in, sign-up, and welcome screens
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = android.view.View.GONE
                }
                R.id.settingsFragment -> {
                    // Hide Toolbar and BottomNavigationView for settings screen
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }
                R.id.umumFragment, R.id.settingNotifikasiFragment, R.id.keamananFragment,
                R.id.pusatBantuanFragment, R.id.tentangFragment -> {
                    // Hide BottomNavigationView for the specified fragments
                    binding.toolbar.visibility = android.view.View.VISIBLE // Keep toolbar visible or manage it as needed
                    binding.navView.visibility = android.view.View.GONE
                }
                R.id.licenseFragment -> {
                    // Hide BottomNavigationView for the LicenseFragment
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }
                R.id.hubungiFragment -> {
                    // Hide BottomNavigationView and show Toolbar for HubungiFragment
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.GONE
                }
                else -> {
                    // Default behavior for other fragments, show the toolbar and bottom navigation
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = android.view.View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable back button for other fragments
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