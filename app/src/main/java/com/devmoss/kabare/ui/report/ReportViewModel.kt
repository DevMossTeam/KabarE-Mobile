package com.devmoss.kabare.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResponseLaporan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel : ViewModel() {

    private val _responseLaporan = MutableLiveData<ResponseLaporan>()
    val responseLaporan: LiveData<ResponseLaporan> get() = _responseLaporan

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun createLaporanBerita(userId: String, beritaId: String, pesan: String, detailPesan: String) {
        val call = ApiConfig.getApiService().createLaporanBerita(userId, beritaId, pesan, detailPesan)
        call.enqueue(object : Callback<ResponseLaporan> {
            override fun onResponse(call: Call<ResponseLaporan>, response: Response<ResponseLaporan>) {
                if (response.isSuccessful) {
                    _responseLaporan.value = response.body()
                } else {
                    _error.value = "Terjadi kesalahan: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<ResponseLaporan>, t: Throwable) {
                _error.value = "Terjadi kesalahan: ${t.message}"
            }
        })
    }

    fun createLaporanKomentar(userId: String, beritaId: String, komenId: String, pesan: String, detailPesan: String) {
        val call = ApiConfig.getApiService().createLaporanKomentar(userId, beritaId, komenId, pesan, detailPesan)
        call.enqueue(object : Callback<ResponseLaporan> {
            override fun onResponse(call: Call<ResponseLaporan>, response: Response<ResponseLaporan>) {
                if (response.isSuccessful) {
                    _responseLaporan.value = response.body()
                } else {
                    _error.value = "Terjadi kesalahan: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseLaporan>, t: Throwable) {
                _error.value = "Terjadi kesalahan: ${t.message}"
            }
        })
    }
}

