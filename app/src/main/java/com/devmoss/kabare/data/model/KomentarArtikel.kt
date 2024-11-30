package com.devmoss.kabare.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class KomentarArtikel(
    val komentarid: String,
    val username: String,
    val profilePicture: String? =null,
    val artikelId: Int,
    val komentar: String,
    val timestamp: Long
) : Parcelable {

}