package com.devmoss.kabare.utils

import java.util.Calendar

fun timeAgo(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val timeDifference = currentTime - timestamp

    val seconds = (timeDifference / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "$seconds detik yang lalu"
        minutes < 60 -> "$minutes menit yang lalu"
        hours < 24 -> "$hours jam yang lalu"
        days < 7 -> "$days hari yang lalu"
        weeks < 4 -> "$weeks minggu yang lalu"
        months < 12 -> {
            // Perhitungan bulan lebih tepat menggunakan Calendar
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val pastMonth = Calendar.getInstance().apply { timeInMillis = timestamp }.get(Calendar.MONTH)
            val monthDiff = currentMonth - pastMonth
            // Jika bulan lebih kecil dari 0 (artinya tahun lalu), tambahkan 12 untuk mendapatkan bulan yang benar
            val correctMonthDiff = if (monthDiff < 0) monthDiff + 12 else monthDiff
            "$correctMonthDiff bulan yang lalu"
        }
        else -> "$years tahun yang lalu"
    }
}
