package com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResponseGetBerita
import com.devmoss.kabare.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeritaTerkiniViewModel : ViewModel() {

    private val _beritaTerkiniList = MutableLiveData<List<ListBerita>>()
    val beritaTerkiniList: LiveData<List<ListBerita>> get() = _beritaTerkiniList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Flag untuk memastikan berita hanya dimuat sekali
    private var isDataLoaded = false


    fun loadBeritaTerkini() {
        // Cek jika data sudah dimuat
        if (isDataLoaded) {
            return // Tidak memuat ulang jika data sudah ada
        }

        _isLoading.value = true
        ApiConfig.getApiService().getBeritaTerkini().enqueue(object : Callback<ResponseGetBerita> {
            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
            }

            override fun onResponse(call: Call<ResponseGetBerita>, response: Response<ResponseGetBerita>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val beritaList = response.body()?.result ?: emptyList()

                    // Log respons untuk memverifikasi data yang diterima
                    Log.d("BeritaTerkini", "Response: ${response.body()}")

                    _beritaTerkiniList.value = beritaList
                    isDataLoaded = true // Tandai data sudah dimuat
                } else {
                    // Log jika respons tidak berhasil
                    Log.e("BeritaTerkini", "Error: ${response.code()}")
                }
            }

        })
    }
}


