package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

// toogle bookmark
@Parcelize
data class ResponseBookmark(

	@field:SerializedName("result")
	val result: ResultBookmark? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultBookmark(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("berita_id")
	val beritaId: String

) : Parcelable


// mengecek status bookmark
@Parcelize
data class BookmarkStatusResponse(

	@field:SerializedName("is_bookmarked")
	val isBookmarked: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
