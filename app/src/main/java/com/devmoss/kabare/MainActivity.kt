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

        // Set Toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // AppBar configuration for specific fragments
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_cari, R.id.navigation_notifications, R.id.navigation_profil)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Add listener to manage visibility of Toolbar and BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Show Toolbar and BottomNavigationView for these fragments
                R.id.navigation_home, R.id.navigation_cari, R.id.navigation_artikel, R.id.navigation_notifications, R.id.navigation_profil -> {
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.navView.visibility = BottomNavigationView.VISIBLE
                }

                // Hide Toolbar and BottomNavigationView for intro and sign-in screens
                R.id.introFragment, R.id.signInFragment, R.id.welcomeFragment -> {
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.navView.visibility = android.view.View.GONE
                }

                // Other fragments can have custom logic if necessary
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}