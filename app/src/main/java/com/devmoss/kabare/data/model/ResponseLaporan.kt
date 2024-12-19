package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseLaporan(

	@field:SerializedName("result")
	val result: ResultLaporan? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultLaporan(

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("status_read")
	val statusRead: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("detail_pesan")
	val detailPesan: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("berita_id")
	val beritaId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
