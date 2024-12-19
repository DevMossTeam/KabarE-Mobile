package com.devmoss.kabare.data.repository

import com.devmoss.kabare.R
import com.devmoss.kabare.data.model.Notifikasi

object NotificationRepository {

    // Simulated data source for notifications
    private val notifications = mutableListOf<Notifikasi>()

    init {
        // Adding some sample notifications
        notifications.add(
            Notifikasi(
                id = 1,
                title = "Willy05",
                titleNotif = "Penelitian Muda SMAN Bali Mandara Raih Prestasi di NASFIA 2024!",
                statusNotifikasi = "Berita Terbaru",
                iconResId = R.drawable.ic_notifications_black_24dp
            )
        )
        notifications.add(
            Notifikasi(
                id = 2,
                title = "Willy05",
                titleNotif = "Sebarkan Rasa Cinta,Kolaborasi ITB dan Kampus Asing",
                statusNotifikasi = "Berita Terbaru",
                iconResId = R.drawable.ic_notifications_black_24dp
            )
        )
        notifications.add(
            Notifikasi(
                id = 3,
                title = "Willy05",
                titleNotif = "Isu Politik Uang,DPR Perketat Aturan dan Sanksi",
                statusNotifikasi = "terbaru",
                iconResId = R.drawable.ic_notifications_black_24dp
            )
        )
    }

    // Function to retrieve notifications
    fun getNotifications(): List<Notifikasi> {
        return notifications
    }

    // Function to add a new notification
    fun addNotification(notification: Notifikasi) {
        notifications.add(notification)
    }
    // Function to remove a notification
    fun removeNotification(notificationId: Int) {
        notifications.removeAll { it.id == notificationId }
    }
}