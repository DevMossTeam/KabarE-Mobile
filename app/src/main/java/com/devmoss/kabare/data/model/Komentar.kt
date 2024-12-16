package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

// Model untuk Response GET Komentar
@Parcelize
data class ResponseGetKomentar(
	@field:SerializedName("result")
	val result: List<ResultGetKomentar>?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

// Model untuk data setiap komentar pada GET
@Parcelize
data class ResultGetKomentar(
	@field:SerializedName("tanggal_komentar")
	val tanggalKomentar: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("nama_pengguna")
	val namaPengguna: String,  // Username of the commenter

	@field:SerializedName("profile_pic")
	val profilePic: String?,  // Profile picture of the user (nullable)

	@field:SerializedName("teks_komentar")
	val teksKomentar: String,  // The text of the comment

	@field:SerializedName("jumlah_komentar")
	val jumlahKomentar: String,

	@field:SerializedName("id")
	val id: String,  // Unique comment ID


	@field:SerializedName("berita_id")
	val beritaId: String  // ID of the associated news
) : Parcelable{

	// Properti untuk konversi tanggalDiterbitkan ke Long
	val dateTimestamp: Long
		get() {
			val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
			return try {
				val date = tanggalKomentar.let { dateFormat.parse(it) }
				date?.time ?: 0L // Kembalikan 0L jika parsing gagal
			} catch (e: Exception) {
				0L // Kembalikan 0L jika terjadi error
			}
		}
}



// Model untuk Response POST Komentar
@Parcelize
data class ResponsePostKomentar(
	@field:SerializedName("status")
	val status: String,  // Status of the response

	@field:SerializedName("message")
	val message: String  // Response message
) : Parcelable

@Parcelize
data class RequestPostKomentar(
	@field:SerializedName("user_id")
	val userId: String,  // User ID who is posting the comment

	@field:SerializedName("berita_id")
	val beritaId: String,  // News ID that the comment is related to

	@field:SerializedName("teks_komentar")
	val teksKomentar: String  // Text content of the comment
) : Parcelable




// Model untuk Response DELETE Komentar
@Parcelize
data class ResponseDeleteKomentar(
	@field:SerializedName("status")
	val status: String,  // Status of the response

	@field:SerializedName("message")
	val message: String  // Response message
) : Parcelable


@Parcelize
data class RequestDeleteKomentar(
	@field:SerializedName("id_komentar")
	val idKomentar: String  // ID of the comment to be deleted
) : Parcelable

