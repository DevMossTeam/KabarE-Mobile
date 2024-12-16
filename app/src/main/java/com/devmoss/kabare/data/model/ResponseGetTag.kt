package com.devmoss.kabare.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetTag(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("result")
    val result: List<Tag>? = null

) : Parcelable

// Data class untuk menyimpan tag terpopuler
@kotlinx.parcelize.Parcelize
data class Tag(

    @field:SerializedName("nama_tag")
    val namaTag: String? = null,

    @field:SerializedName("jumlah_tag")
    val jumlahTag: Int? = null

) : Parcelable
