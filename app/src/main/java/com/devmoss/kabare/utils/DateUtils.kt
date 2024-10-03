package com.devmoss.kabare.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
