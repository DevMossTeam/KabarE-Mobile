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
        handlePermissionChange(isGranted)
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
        createNotificationChannel()
        loadSettings()
        setupListeners()

        return view
    }

    private fun loadSettings() {
        // Load the preferences and set the switches accordingly
        val isNotificationAllowed = isNotificationPermissionGranted()

        // Set main notification switch state based on permission
        switchNotifikasi.isChecked = isNotificationAllowed && sharedPreferences.getBoolean(KEY_NOTIFIKASI, true)
        switchBeritaTerbaru.isChecked = isNotificationAllowed && sharedPreferences.getBoolean(KEY_BERITA_TERBARU, true)
        switchBeritaPopuler.isChecked = isNotificationAllowed && sharedPreferences.getBoolean(KEY_BERITA_POPULER, true)

        // Enable or disable switches based on notification permission
        setCategorySwitchesEnabled(isNotificationAllowed && switchNotifikasi.isChecked)
    }

    private fun setupListeners() {
        switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isNotificationPermissionGranted()) {
                // Request permission if user tries to enable notifications
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                savePreference(KEY_NOTIFIKASI, isChecked)
                setCategorySwitchesEnabled(isChecked)
                if (!isChecked) disableAllCategorySwitches()
            }
        }

        setupCategorySwitchListener(switchBeritaTerbaru, KEY_BERITA_TERBARU, "Berita Terbaru")
        setupCategorySwitchListener(switchBeritaPopuler, KEY_BERITA_POPULER, "Berita Populer")
    }

    private fun setupCategorySwitchListener(switch: Switch, key: String, title: String) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            savePreference(key, isChecked)
            sendNotification(title, "$title telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}")
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    private fun handlePermissionChange(isGranted: Boolean) {
        if (isGranted) {
            switchNotifikasi.isChecked = true
            setCategorySwitchesEnabled(true)
        } else {
            switchNotifikasi.isChecked = false
            disableAllCategorySwitches()
        }
    }

    private fun disableAllCategorySwitches() {
        switchBeritaTerbaru.isChecked = false
        switchBeritaPopuler.isChecked = false

        savePreference(KEY_BERITA_TERBARU, false)
        savePreference(KEY_BERITA_POPULER, false)
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
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = getString(R.string.notification_channel_description)
                    setSound(
                        Uri.parse("android.resource://${requireContext().packageName}/raw/kabare"),
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun sendNotification(title: String, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Jika izin belum diberikan, log atau beri tahu pengguna
                return
            }
        }

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
            .setSmallIcon(R.drawable.kabare) // Replace with your icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(requireContext()).notify(NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            // Tangani exception jika terjadi
            e.printStackTrace()
        }
    }
}