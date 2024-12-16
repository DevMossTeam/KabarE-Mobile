package com.devmoss.kabare.ui.home.detailberita.detailviewmodels

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

class BeritaTerkaitViewModel : ViewModel() {

    private val _beritaTerkaitList = MutableLiveData<List<ListBerita>>()
    val beritaTerkaitList: LiveData<List<ListBerita>> = _beritaTerkaitList

    private val apiService = ApiConfig.getApiService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadBeritaTerkait(kategori: String, beritaId: String, forceReload: Boolean = false) {
        // Memuat ulang berita terkait tanpa flag isDataLoaded
        _isLoading.value = true
        apiService.getBeritaTerkait(kategori, beritaId)
            .enqueue(object : Callback<ResponseGetBerita> {
                override fun onResponse(
                    call: Call<ResponseGetBerita>,
                    response: Response<ResponseGetBerita>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val beritaTerkait = response.body()?.result.orEmpty()
                        _beritaTerkaitList.value = beritaTerkait
                    } else {
                        Log.e(
                            "BeritaTerkaitViewModel",
                            "Gagal memuat berita terkait: ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("BeritaTerkaitViewModel", "Kesalahan jaringan: ${t.message}")
                }
            })
    }
}
