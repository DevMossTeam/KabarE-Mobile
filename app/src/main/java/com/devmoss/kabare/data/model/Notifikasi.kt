package com.devmoss.kabare.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Notifikasi (
    val id: Int,
    val title: String,
    val titleNotif: String,
    val statusNotifikasi: String,
    val iconResId: Int
) : Parcelable {
}