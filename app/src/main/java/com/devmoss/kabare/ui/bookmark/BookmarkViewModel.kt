package com.devmoss.kabare.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResponseBookmark
import com.devmoss.kabare.data.model.ResultBookmark
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkViewModel : ViewModel() {

    // API service untuk berkomunikasi dengan backend
    private val apiService = ApiConfig.getApiService()

    // LiveData untuk menyimpan daftar bookmark
    private val _bookmarks = MutableLiveData<List<ResultBookmark>>()
    val bookmarks: LiveData<List<ResultBookmark>> get() = _bookmarks

    // LiveData untuk status toggle bookmark
    private val _bookmarkStatus = MutableLiveData<String>()
    val bookmarkStatus: LiveData<String> get() = _bookmarkStatus

    // Fungsi untuk menambah atau menghapus bookmark
    fun toggleBookmark(resultBookmark: ResultBookmark) {
        apiService.addBookmark(resultBookmark).enqueue(object : Callback<ResponseBookmark> {
            override fun onResponse(call: Call<ResponseBookmark>, response: Response<ResponseBookmark>) {
                if (response.isSuccessful) {
                    // Setelah berhasil, ambil daftar bookmark yang terbaru
                    _bookmarkStatus.value = "Bookmark berhasil ditambahkan."
                } else {
                    _bookmarkStatus.value = "Gagal menambah bookmark: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseBookmark>, t: Throwable) {
                _bookmarkStatus.value = "Terjadi kesalahan: ${t.message}"
            }
        })
    }

}

