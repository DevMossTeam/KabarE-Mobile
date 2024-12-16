package com.devmoss.kabare.ui.komentarartikel.komentarviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResponseDeleteKomentar
import com.devmoss.kabare.data.model.ResponseGetKomentar
import com.devmoss.kabare.data.model.ResponsePostKomentar
import com.devmoss.kabare.data.model.ResultGetKomentar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KomentarViewModel : ViewModel() {

    // LiveData untuk komentar
    private val _komentarList = MutableLiveData<List<ResultGetKomentar>>()
    val komentarList: LiveData<List<ResultGetKomentar>> get() = _komentarList

    // LiveData untuk status operasi (POST, DELETE)
    private val _statusKomentar = MutableLiveData<String>()
    val statusKomentar: LiveData<String> get() = _statusKomentar

    private val _deleteKomentarStatus = MutableLiveData<String>() // Status penghapusan
    val deleteKomentarStatus: LiveData<String> get() = _deleteKomentarStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isInitialLoad = MutableLiveData<Boolean>()
    val isInitialLoad: LiveData<Boolean> get() = _isInitialLoad



    private val apiInterface = ApiConfig.getApiService()

    // Fungsi untuk mendapatkan komentar berdasarkan berita_id
    fun getKomentar(beritaId: String, isInitialLoad: Boolean = false) {
        if (isInitialLoad) {
            _isInitialLoad.value = true // Tampilkan shimmer untuk loading awal
        } else {
            _isLoading.value = true // Loading tanpa shimmer
        }

        if (beritaId.isBlank()) {
            Log.e("KomentarViewModel", "ID Berita kosong atau null")
            _isInitialLoad.value = false
            _isLoading.value = false
            return
        }

        apiInterface.getKomentar(beritaId).enqueue(object : Callback<ResponseGetKomentar> {
            override fun onResponse(call: Call<ResponseGetKomentar>, response: Response<ResponseGetKomentar>) {
                _isInitialLoad.value = false // Hentikan shimmer
                _isLoading.value = false
                if (response.isSuccessful) {
                    _komentarList.value = response.body()?.result ?: emptyList()
                } else {
                    Log.e("KomentarViewModel", "Error API: ${response.message()}")
                    _komentarList.value = emptyList()
                }
            }

            override fun onFailure(call: Call<ResponseGetKomentar>, t: Throwable) {
                _isInitialLoad.value = false
                _isLoading.value = false
                Log.e("KomentarViewModel", "API request gagal: ${t.message}")
            }
        })
    }



    // Fungsi POST komentar
    fun postKomentar(userId: String, beritaId: String, teksKomentar: String) {
        // Validasi data
        if (userId.isBlank() || beritaId.isBlank() || teksKomentar.isBlank()) {
            Log.e("KomentarViewModel", "Input tidak valid: userId=$userId, beritaId=$beritaId, teksKomentar=$teksKomentar")
            _statusKomentar.postValue("error")
            return
        }

        apiInterface.postKomentar(userId, beritaId, teksKomentar)
            .enqueue(object : Callback<ResponsePostKomentar> {
                override fun onResponse(
                    call: Call<ResponsePostKomentar>,
                    response: Response<ResponsePostKomentar>
                ) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        Log.d("Komentar", "Berhasil: ${response.body()?.message}")
                        _statusKomentar.postValue("success")
                    } else {
                        Log.e("Komentar", "Gagal: ${response.body()?.message}")
                        _statusKomentar.postValue("error")
                    }
                }
                override fun onFailure(call: Call<ResponsePostKomentar>, t: Throwable) {
                    Log.e("KomentarViewModel", "Gagal menghubungi server: ${t.localizedMessage}")
                    _statusKomentar.postValue("error")
                }
            })
    }

    fun deleteKomentar(id: String) {
        Log.d("KomentarViewModel", "Menghapus komentar dengan ID: $id")
        apiInterface.deleteKomentar(id).enqueue(object : Callback<ResponseDeleteKomentar> {
            override fun onResponse(call: Call<ResponseDeleteKomentar>, response: Response<ResponseDeleteKomentar>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Log.d("KomentarViewModel", "Komentar berhasil dihapus: ${response.body()?.message}")
                    _deleteKomentarStatus.postValue("success") // Mengatur status ke "success"
                } else {
                    Log.e("KomentarViewModel", "Gagal menghapus komentar: ${response.errorBody()?.string()}")
                    _deleteKomentarStatus.postValue("error") // Mengatur status ke "error"
                }
            }

            override fun onFailure(call: Call<ResponseDeleteKomentar>, t: Throwable) {
                Log.e("KomentarViewModel", "Gagal menghapus komentar", t)
                _deleteKomentarStatus.postValue("error") // Mengatur status ke "error"
            }
        })
    }
}

