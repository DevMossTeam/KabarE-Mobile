package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale



@Parcelize
data class ResponseGetBerita(

	@field:SerializedName("result")
	val result: List<ListBerita>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ListBerita(

	@field:SerializedName("id")
	val idBerita: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("status_publikasi")
	val statusPublikasi: String? = null,

	@field:SerializedName("tanggal_dibuat")
	val tanggalDibuat: String? = null,

	@field:SerializedName("view_count")
	val viewCount: Int? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("tanggal_diterbitkan")
	val tanggalTerbit: String? = null,

	@field:SerializedName("konten_artikel")
	val kontenBerita: String? = null,

	@field:SerializedName("nama_pengguna")
	val author: String? = null,

	@field:SerializedName("profile_pic")
	val profilePic: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("jumlah_suka")
	val jumlahLike: Int? = null,

	@field:SerializedName("jumlah_tidak_suka")
	val jumlahDislike: Int? = null,

	@field:SerializedName("jumlah_komentar")
	val jumlahKomentar: Int? = null,

//	var isBookmarked: Boolean = false // Status bookmark

) : Parcelable {


	// Properti untuk konversi tanggalDiterbitkan ke Long
	val tanggalDiterbitkan: Long
		get() {
			val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
			return try {
				val date = tanggalTerbit?.let { dateFormat.parse(it) }
				date?.time ?: 0L // Kembalikan 0L jika parsing gagal
			} catch (e: Exception) {
				0L // Kembalikan 0L jika terjadi error
			}
		}

}