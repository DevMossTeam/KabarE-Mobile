package com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels

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

    // Flag untuk memastikan berita hanya dimuat sekali
    private var isDataLoaded = false

    fun loadBeritaTerkait(kategori: String, beritaTerkiniList: List<ListBerita>) {
        // Cek jika data sudah dimuat
        if (isDataLoaded) {
            return // Tidak memuat ulang jika data sudah ada
        }

        val beritaId = beritaTerkiniList.firstOrNull()?.idBerita ?: ""

        // Log untuk memastikan bahwa parameter kategori dan beritaId sudah benar
        Log.d("BeritaTerkait", "Kategori: $kategori, ID Berita: $beritaId")

        // Pastikan kategori dan beritaId tidak kosong
        if (kategori.isEmpty() || beritaId.isEmpty()) {
            Log.e("BeritaTerkait", "Kategori atau ID Berita kosong!")
            return
        }

        // Memanggil API untuk mendapatkan berita terkait
        apiService.getBeritaTerkait(kategori, beritaId).enqueue(object : Callback<ResponseGetBerita> {
            override fun onResponse(call: Call<ResponseGetBerita>, response: Response<ResponseGetBerita>) {
                if (response.isSuccessful) {
                    val beritaTerkait = response.body()?.result.orEmpty()
                    Log.d("BeritaTerkait", "Data diterima: $beritaTerkait")

                    // Periksa apakah data valid dan tidak kosong
                    if (beritaTerkait.isNotEmpty()) {
                        _beritaTerkaitList.value = beritaTerkait
                        isDataLoaded = true // Tandai data sudah dimuat
                    } else {
                        Log.e("BeritaTerkait", "Tidak ada berita terkait yang ditemukan.")
                    }
                } else {
                    Log.e("BeritaTerkait", "Response gagal: ${response.message()} - ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                Log.e("BeritaTerkaitViewModel", "Error: ${t.message}")
            }
        })
    }
}

