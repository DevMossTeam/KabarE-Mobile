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
                title = "New Article Published",
                titleNotif = "A new article has been published on Kabare!",
                statusNotifikasi = "Ditolak",
                iconResId = R.drawable.ic_notifikasi_ditolak
            )
        )
        notifications.add(
            Notifikasi(
                id = 2,
                title = "Weekly Summary",
                titleNotif = "Here is your weekly summary of articles.",
                statusNotifikasi = "Ditolak",
                iconResId = R.drawable.ic_notifikasi_ditolak
            )
        )
        notifications.add(
            Notifikasi(
                id = 3,
                title = "New Comment",
                titleNotif = "Someone commented on your article.",
                statusNotifikasi = "Ditolak",
                iconResId = R.drawable.ic_notifikasi_ditolak
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
