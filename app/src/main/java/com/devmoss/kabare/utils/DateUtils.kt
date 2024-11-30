package com.devmoss.kabare.utils

fun timeAgo(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val timeDifference = currentTime - timestamp

    val seconds = (timeDifference / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "$seconds detik yang lalu"
        minutes < 60 -> "$minutes menit yang lalu"
        hours < 24 -> "$hours jam yang lalu"
        else -> "$days hari yang lalu"
    }
}
