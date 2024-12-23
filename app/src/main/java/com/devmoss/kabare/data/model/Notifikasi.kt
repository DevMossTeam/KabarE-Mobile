package com.devmoss.kabare.data.model

data class Notifikasi(
    val judul: String,       // Title of the notification/news item
    val tanggal_diterbitkan: String    // Use 'createdAt' instead of 'time' to match the API response
)