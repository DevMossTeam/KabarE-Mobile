package com.devmoss.kabare.ui.profile.settings

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import android.media.RingtoneManager
import android.net.Uri
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Permission denied. Show a message to the user explaining the permission requirement
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settingnotifikasi, container, false)

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        switchNotifikasi = view.findViewById(R.id.switch_notifikasi)
        switchBeritaTerbaru = view.findViewById(R.id.switch_berita_terbaru)
        switchBeritaPopuler = view.findViewById(R.id.switch_berita_populer)

        createNotificationChannel()
        loadSettings()
        setupListeners()

        return view
    }

    private fun loadSettings() {
        val notifikasiEnabled = sharedPreferences.getBoolean(KEY_NOTIFIKASI, true)
        val beritaTerbaruEnabled = sharedPreferences.getBoolean(KEY_BERITA_TERBARU, true)
        val beritaPopulerEnabled = sharedPreferences.getBoolean(KEY_BERITA_POPULER, false)

        switchNotifikasi.isChecked = notifikasiEnabled
        switchBeritaTerbaru.isChecked = beritaTerbaruEnabled
        switchBeritaPopuler.isChecked = beritaPopulerEnabled

        setCategorySwitchesEnabled(notifikasiEnabled)
    }

    private fun setupListeners() {
        switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            savePreference(KEY_NOTIFIKASI, isChecked)
            setCategorySwitchesEnabled(isChecked)
            checkAndRequestNotificationPermission()
            sendNotification(
                "Notifikasi",
                "Notifikasi telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}"
            )
        }

        switchBeritaTerbaru.setOnCheckedChangeListener { _, isChecked ->
            savePreference(KEY_BERITA_TERBARU, isChecked)
            checkAndRequestNotificationPermission()
            sendNotification(
                "Berita Terbaru",
                "Berita Terbaru telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}"
            )
        }

        switchBeritaPopuler.setOnCheckedChangeListener { _, isChecked ->
            savePreference(KEY_BERITA_POPULER, isChecked)
            checkAndRequestNotificationPermission()
            sendNotification(
                "Berita Populer",
                "Berita Populer telah ${if (isChecked) "diaktifkan" else "dinonaktifkan"}"
            )
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
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
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Menghapus channel lama jika sudah ada
            notificationManager.deleteNotificationChannel(CHANNEL_ID)

            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val soundUri = Uri.parse("android.resource://${requireContext().packageName}/raw/kabare")

            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()

            // Membuat channel dengan pengaturan suara kustom
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(soundUri, audioAttributes)
            }

            // Mendaftarkan channel dengan sistem
            notificationManager.createNotificationChannel(channel)
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
            .setSmallIcon(R.drawable.kabare) // Ganti dengan ikon notifikasi yang sesuai
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setSound(soundUri) // Mengatur nada dering kustom pada notifikasi
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(NOTIFICATION_ID, builder.build())
                } else {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                }
            } else {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}