package com.devmoss.kabare.ui.home.detailberita.detailviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArtikelViewModel : ViewModel() {

    // LiveData untuk menyimpan data artikel dalam bentuk List
    private val _beritaDetail = MutableLiveData<List<ListBerita>?>()
    val beritaDetail: LiveData<List<ListBerita>?> = _beritaDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _statusLike = MutableLiveData<ReaksiResponse?>()
    val statusLike: LiveData<ReaksiResponse?> = _statusLike

    private fun setLoading(state: Boolean) {
        if (_isLoading.value != state) {
            _isLoading.value = state
        }
    }

    // Mendapatkan detail berita dari API
    fun getDetailBerita(beritaId: String) {
        setLoading(true)
        ApiConfig.getApiService().getDetailBerita(beritaId).enqueue(object :
            Callback<ResponseGetBerita> {
            override fun onResponse(
                call: Call<ResponseGetBerita>,
                response: Response<ResponseGetBerita>
            ) {
                setLoading(false)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val details = response.body()?.result
                    _beritaDetail.value = details
                } else {
                    _beritaDetail.value = null
                }
            }

            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                setLoading(false)
                _beritaDetail.value = null
                Log.e("DetailBeritaError", "Error: ${t.message}")
            }
        })
    }

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

        setLoading(true)
        ApiConfig.getApiService().postReaksi(reaksiRequest).enqueue(object :
            Callback<ResponseReaksi> {
            override fun onResponse(call: Call<ResponseReaksi>, response: Response<ResponseReaksi>) {
                setLoading(false)
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
                setLoading(false)
                Log.e("ReaksiFailure", "Error: ${t.message}")
                onResult("Koneksi gagal: ${t.message}")
            }
        })
    }

    // Fungsi untuk mendapatkan jumlah reaksi
    fun getJumlahReaksi(beritaId: String, onResult: (ResponseJumlahReaksi?) -> Unit) {
        setLoading(true)
        ApiConfig.getApiService().getJumlahReaksi(beritaId).enqueue(object :
            Callback<ResponseJumlahReaksi> {
            override fun onResponse(
                call: Call<ResponseJumlahReaksi>,
                response: Response<ResponseJumlahReaksi>
            ) {
                setLoading(false)
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("JumlahReaksiError", "Error: $errorBody")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResponseJumlahReaksi>, t: Throwable) {
                setLoading(false)
                Log.e("JumlahReaksiFailure", "Error: ${t.message}")
                onResult(null)
            }
        })
    }

    // Fungsi untuk mendapatkan status like
    fun getStatusLike(userId: String, beritaId: String) {
        setLoading(true)
        ApiConfig.getApiService().cekStatusLike(userId, beritaId).enqueue(object :
            Callback<ReaksiResponse> {
            override fun onResponse(call: Call<ReaksiResponse>, response: Response<ReaksiResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    _statusLike.value = response.body()
                } else {
                    _statusLike.value = null
                }
            }

            override fun onFailure(call: Call<ReaksiResponse>, t: Throwable) {
                setLoading(false)
                _statusLike.value = null
                Log.e("StatusLikeError", "Error: ${t.message}")
            }
        })
    }
}
