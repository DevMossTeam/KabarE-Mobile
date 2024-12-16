package com.devmoss.kabare.ui.home.populer.populerviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResponseGetBerita
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeritaRekomendasiViewModel : ViewModel() {
    private val _beritaRekomendasiList = MutableLiveData<List<ListBerita>>()
    val beritaRekomendasiList: LiveData<List<ListBerita>> get() = _beritaRekomendasiList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadBeritaRekomendasi(userId: String) {

        _isLoading.value = true
        val client = ApiConfig.getApiService().getBeritaRekomendasi(userId)
        client.enqueue(object : Callback<ResponseGetBerita> {
            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
            }

            override fun onResponse(
                call: Call<ResponseGetBerita>,
                response: Response<ResponseGetBerita>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _beritaRekomendasiList.value = response.body()?.result ?: emptyList()
                }
            }
        })
    }
}