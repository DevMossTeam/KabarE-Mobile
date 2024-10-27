package com.devmoss.kabare.ui.notifikasi.notifikasiviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Notifikasi
import com.devmoss.kabare.data.repository.NotificationRepository

class NotificationViewModel : ViewModel() {
    // LiveData untuk menyimpan daftar notifikasi
    private val _notificationsLiveData = MutableLiveData<List<Notifikasi>>()
    val notificationsLiveData: LiveData<List<Notifikasi>> get() = _notificationsLiveData

    init {
        loadNotifications() // Memuat notifikasi saat ViewModel diinisialisasi
    }

    private fun loadNotifications() {
        // Mengambil notifikasi dari repository
        _notificationsLiveData.value = NotificationRepository.getNotifications()
    }

    // Fungsi untuk menambahkan notifikasi
    fun addNotification(notification: Notifikasi) {
        NotificationRepository.addNotification(notification)
        loadNotifications() // Memuat ulang notifikasi
    }

    // Fungsi untuk menghapus notifikasi
    fun removeNotification(notificationId: Int) {
        NotificationRepository.removeNotification(notificationId)
        loadNotifications() // Memuat ulang notifikasi
    }
}