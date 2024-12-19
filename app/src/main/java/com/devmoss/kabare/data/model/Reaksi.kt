package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseReaksi(

	@field:SerializedName("result")
	val result: ReaksiRequest? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ReaksiRequest(

	@field:SerializedName("berita_id")
	val beritaId: String,

	@field:SerializedName("aksi")
	val aksi: String,

	@field:SerializedName("user_id")
	val userId: String
) : Parcelable

@Parcelize
data class ResponseJumlahReaksi(

	@field:SerializedName("result")
	val result: ResultJumlahReaksi? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultJumlahReaksi(

	@field:SerializedName("jumlah_tidak_suka")
	val jumlahTidakSuka: Int? = 0, // Jumlah reaksi Tidak Suka

	@field:SerializedName("jumlah_suka")
	val jumlahSuka: Int? = 0 // Jumlah reaksi Suka

) : Parcelable

// response status like dislike
@Parcelize
data class ReaksiResponse(

	@field:SerializedName("is_dislike")
	val isDislike: Int? = null,

	@field:SerializedName("is_like")
	val isLike: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
