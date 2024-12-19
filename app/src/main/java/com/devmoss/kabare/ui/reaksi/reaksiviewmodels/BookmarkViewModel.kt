package com.devmoss.kabare.ui.reaksi.reaksiviewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.BookmarkStatusResponse
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResponseBookmark
import com.devmoss.kabare.data.model.ResultBookmark
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    val bookmarkStatusMap = MutableLiveData<Map<String, Boolean>>()

    // Status bookmark hanya dicek sekali saat tampilan pertama kali dimuat
    fun checkBookmarkStatus(userId: String, beritaList: List<ListBerita>) {
        val statusMap = mutableMapOf<String, Boolean>()

        beritaList.forEach { berita ->
            val beritaId = berita.idBerita
            statusMap[beritaId ?: ""] = false  // Set status default
        }

        bookmarkStatusMap.postValue(statusMap)

        var remainingRequests = beritaList.size

        beritaList.forEach { berita ->
            val beritaId = berita.idBerita ?: return@forEach
            apiService.checkBookmarkStatus(userId, beritaId).enqueue(object : Callback<BookmarkStatusResponse> {
                override fun onResponse(
                    call: Call<BookmarkStatusResponse>,
                    response: Response<BookmarkStatusResponse>
                ) {
                    if (response.isSuccessful) {
                        val isBookmarked = response.body()?.isBookmarked == 1
                        statusMap[beritaId] = isBookmarked
                    }
                    remainingRequests--
                    if (remainingRequests == 0) {
                        bookmarkStatusMap.postValue(statusMap) // Hanya update jika semua request selesai
                    }
                }

                override fun onFailure(call: Call<BookmarkStatusResponse>, t: Throwable) {
                    statusMap[beritaId] = false
                    remainingRequests--
                    if (remainingRequests == 0) {
                        bookmarkStatusMap.postValue(statusMap) // Hanya update jika semua request selesai
                    }
                }
            })
        }
    }


    fun toggleBookmark(resultBookmark: ResultBookmark, context: Context) {
        apiService.addBookmark(resultBookmark).enqueue(object : Callback<ResponseBookmark> {
            override fun onResponse(call: Call<ResponseBookmark>, response: Response<ResponseBookmark>) {
                // Mengambil status bookmark yang ada, bisa null, jika null, buat map baru
                val updatedStatus = bookmarkStatusMap.value?.toMutableMap() ?: mutableMapOf()

                // Pastikan beritaId bisa menjadi string
                val beritaId = resultBookmark.beritaId

                // Cek apakah status bookmark sudah ada atau tidak
                val currentStatus = updatedStatus[beritaId]
                if (currentStatus != null) {
                    // Jika status sudah ada, toggle statusnya
                    updatedStatus[beritaId] = !currentStatus
                } else {
                    // Jika null, set status default untuk beritaId tersebut (misalnya, true)
                    updatedStatus[beritaId] = true
                }

                // Post ke LiveData untuk memperbarui status bookmark
                bookmarkStatusMap.postValue(updatedStatus)

                // Menampilkan Toast message berdasarkan response API
                val message = response.body()?.message ?: "Terjadi kesalahan"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBookmark>, t: Throwable) {
                // Handle failure
                Toast.makeText(context, "Gagal menambahkan bookmark", Toast.LENGTH_SHORT).show()
            }
        })
    }

}




