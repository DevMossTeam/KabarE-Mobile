package com.devmoss.kabare.ui.reaksi.reaksiviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResponseGetBerita
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarBeritaBookmarkViewModel : ViewModel() {

    private val _beritaBookmarkList = MutableLiveData<List<ListBerita>>()
    val beritaBookmarkList: LiveData<List<ListBerita>> = _beritaBookmarkList

    private val apiService = ApiConfig.getApiService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadBeritaBookmark(userId: String) {
        _isLoading.value = true
        Log.d("DaftarBeritaBookmarkViewModel", "Memuat berita bookmark untuk userId: $userId")

        if (userId.isEmpty()) {
            Log.e("DaftarBeritaBookmarkViewModel", "User ID kosong")
            return
        }

        apiService.getBeritaBookmark(userId).enqueue(object :Callback<ResponseGetBerita> {
            override fun onResponse(call: Call<ResponseGetBerita>, response: Response<ResponseGetBerita>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val beritaTerkait = response.body()?.result.orEmpty()
                    _beritaBookmarkList.value = beritaTerkait
                } else {
                    Log.e("DaftarBeritaBookmarkViewModel", "Gagal memuat berita bookmark: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
                Log.e("DaftarBeritaBookmarkViewModel", "Kesalahan jaringan: ${t.message}")
            }
        })
    }


}