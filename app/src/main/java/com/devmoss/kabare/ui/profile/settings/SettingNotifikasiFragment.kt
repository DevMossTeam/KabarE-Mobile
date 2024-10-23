package com.devmoss.kabare.ui.profile.settings

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.devmoss.kabare.MainActivity
import com.devmoss.kabare.R

class SettingNotifikasiFragment : Fragment() {

    private lateinit var switchNotifikasi: Switch
    private lateinit var switchBeritaTerbaru: Switch
    private lateinit var switchBeritaPopuler: Switch
    private lateinit var switchInteraksiPengguna: Switch
    private lateinit var switchBeritaTerkait: Switch
    private lateinit var switchPengingat: Switch
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFS_NAME = "notification_settings"
        const val KEY_NOTIFIKASI = "notifikasi"
        const val KEY_BERITA_TERBARU = "berita_terbaru"
        const val KEY_BERITA_POPULER = "berita_populer"
        const val CHANNEL_ID = "notification_channel"
        const val NOTIFICATION_ID = 1
    }

    // Register for permission request result
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Inform the user why notification permission is necessary
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settingnotifikasi, container, false)

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Initialize switches
        switchNotifikasi = view.findViewById(R.id.switch_notifikasi)
        switchBeritaTerbaru = view.findViewById(R.id.switch_berita_terbaru)
        switchBeritaPopuler = view.findViewById(R.id.switch_berita_populer)
        switchInteraksiPengguna = view.findViewById(R.id.switch_interaksi_pengguna)
        switchBeritaTerkait = view.findViewById(R.id.switch_berita_terkait)
        switchPengingat = view.findViewById(R.id.switch_pengingat)

        createNotificationChannel() // Create the notification channel
        loadSettings() // Load saved settings
        setupListeners() // Set up listener for switches

        return view
    }

    private fun loadSettings() {
        // Load the preferences and set the switches accordingly
        switchNotifikasi.isChecked = sharedPreferences.getBoolean(KEY_NOTIFIKASI, true)
        switchBeritaTerbaru.isChecked = sharedPreferences.getBoolean(KEY_BERITA_TERBARU, true)
        switchBeritaPopuler.isChecked = sharedPreferences.getBoolean(KEY_BERITA_POPULER, false)
        switchInteraksiPengguna.isChecked = sharedPreferences.getBoolean("interaksi_pengguna", true)
        switchBeritaTerkait.isChecked = sharedPreferences.getBoolean("berita_terkait", true)
        switchPengingat.isChecked = sharedPreferences.getBoolean("pengingat", true)

        // Enable or disable category switches based on notification setting
        setCategorySwitchesEnabled(switchNotifikasi.isChecked)
    }

    private fun setupListeners() {
        // Set up listener for main notification switch
        switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            savePreference(KEY_NOTIFIKASI, isChecked)
            setCategorySwitchesEnabled(isChecked)
            checkAndRequestNotificationPermission()
            sendNotification("Notifikasi", "Notifikasi telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}")

            // If notifications are disabled, ensure the other switches are also unchecked
            if (!isChecked) {
                switchBeritaTerbaru.isChecked = false
                switchBeritaPopuler.isChecked = false
                switchInteraksiPengguna.isChecked = false
                switchBeritaTerkait.isChecked = false
                switchPengingat.isChecked = false
                savePreference(KEY_BERITA_TERBARU, false)
                savePreference(KEY_BERITA_POPULER, false)
                savePreference("interaksi_pengguna", false)
                savePreference("berita_terkait", false)
                savePreference("pengingat", false)
            }
        }

        // Set up listeners for each category switch
        setupCategorySwitchListener(switchBeritaTerbaru, KEY_BERITA_TERBARU, "Berita Terbaru")
        setupCategorySwitchListener(switchBeritaPopuler, KEY_BERITA_POPULER, "Berita Populer")
        setupCategorySwitchListener(switchInteraksiPengguna, "interaksi_pengguna", "Interaksi Pengguna")
        setupCategorySwitchListener(switchBeritaTerkait, "berita_terkait", "Berita Terkait")
        setupCategorySwitchListener(switchPengingat, "pengingat", "Pengingat")
    }

    private fun setupCategorySwitchListener(switch: Switch, key: String, title: String) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            savePreference(key, isChecked)
            checkAndRequestNotificationPermission()
            sendNotification(title, "$title telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}")
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun savePreference(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    private fun setCategorySwitchesEnabled(enabled: Boolean) {
        switchBeritaTerbaru.isEnabled = enabled
        switchBeritaPopuler.isEnabled = enabled
        switchInteraksiPengguna.isEnabled = enabled
        switchBeritaTerkait.isEnabled = enabled
        switchPengingat.isEnabled = enabled
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Check if channel already exists
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val name = getString(R.string.notification_channel_name)
                val descriptionText = getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val soundUri = Uri.parse("android.resource://${requireContext().packageName}/raw/kabare")

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

                // Create the notification channel
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    setSound(soundUri, audioAttributes)
                }

                // Register the channel with the system
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun sendNotification(title: String, content: String) {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = Uri.parse("android.resource://${requireContext().packageName}/raw/kabare")

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.kabare) // Change to your notification icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setSound(soundUri) // Set custom sound for notification
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notify(NOTIFICATION_ID, builder.build())
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}
