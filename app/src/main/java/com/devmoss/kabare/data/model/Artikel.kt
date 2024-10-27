package com.devmoss.kabare.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Artikel(
    val id: Int,
    val image: ByteArray? = null,
    val title: String,
    val description: String,  // Opsional, deskripsi bisa tidak ada di draf atau peninjauan
    val kategori: String,
    val timestamp: Long,
    val label:List<String>,
    val authorProfile: String? = null,
    val jumlahLike: Int = 0,
    val jumlahDislike: Int = 0,
    val jumlahShare: Int = 0,
    var isBookmarked: Boolean = false,
    var isTerkini: Boolean = false,
    var isTeratas: Boolean = false,
    val status: StatusArtikel,
    val tanggalPengajuan: String,  // Hanya digunakan untuk artikel dalam peninjauan
    val tanggalDiperbarui: String,  // Digunakan di peninjauan dan diterbitkan
    val statusPeninjauan: StatusPeninjauan // Hanya di dalam peninjauan
) : Parcelable

// Enum untuk status artikel
enum class StatusArtikel {
    DRAF,DALAM_PENINJAUAN,DITERBITKAN
}

enum class StatusPeninjauan(val status: String) {
    DIAJUKAN("Diajukan"),
    DITOLAK("Ditolak"),
    REVISI_KECIL("Revisi Kecil"),
    REVISI_BESAR("Revisi Besar"),
    PUBLISHED("Published") // Huruf kapital untuk konstanta enum
}


