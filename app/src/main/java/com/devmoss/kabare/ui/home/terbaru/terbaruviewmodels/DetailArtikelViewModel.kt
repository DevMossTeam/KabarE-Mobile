package com.devmoss.kabare.ui.home.terbaru.terbaruviewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ReaksiRequest
import com.devmoss.kabare.data.model.ResponseJumlahReaksi
import com.devmoss.kabare.data.model.ResponseReaksi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArtikelViewModel : ViewModel() {

    // Fungsi untuk mengirim reaksi
    fun sendReaksi(beritaId: String, userId: String, jenisReaksi: String, onResult: (String) -> Unit) {
        if (beritaId.isEmpty() || userId.isEmpty() || jenisReaksi.isEmpty()) {
            Log.e("ReaksiError", "User ID, Berita ID, dan Aksi harus disertakan.")
            onResult("User ID, Berita ID, dan Aksi harus disertakan.")
            return
        }

        val reaksiRequest = ReaksiRequest(
            beritaId = beritaId,
            userId = userId,
            aksi = jenisReaksi
        )

        ApiConfig.getApiService().postReaksi(reaksiRequest).enqueue(object : Callback<ResponseReaksi> {
            override fun onResponse(call: Call<ResponseReaksi>, response: Response<ResponseReaksi>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Reaksi berhasil"
                    onResult(message)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ReaksiError", "Error: $errorBody")
                    onResult("Gagal mengirim reaksi")
                }
            }

            override fun onFailure(call: Call<ResponseReaksi>, t: Throwable) {
                Log.e("ReaksiFailure", "Error: ${t.message}")
                onResult("Koneksi gagal: ${t.message}")
            }
        })
    }

    // Fungsi untuk mendapatkan jumlah reaksi
    fun getJumlahReaksi(beritaId: String, onResult: (ResponseJumlahReaksi?) -> Unit) {
        ApiConfig.getApiService().getJumlahReaksi(beritaId).enqueue(object : Callback<ResponseJumlahReaksi> {
            override fun onResponse(
                call: Call<ResponseJumlahReaksi>,
                response: Response<ResponseJumlahReaksi>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("JumlahReaksiError", "Error: $errorBody")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResponseJumlahReaksi>, t: Throwable) {
                Log.e("JumlahReaksiFailure", "Error: ${t.message}")
                onResult(null)
            }
        })
    }
}
