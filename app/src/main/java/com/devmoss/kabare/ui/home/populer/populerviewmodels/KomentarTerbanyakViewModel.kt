package com.devmoss.kabare.ui.home.populer.populerviewmodels

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

class KomentarTerbanyakViewModel : ViewModel()  {
    private val _komentarTerbanyakList = MutableLiveData<List<ListBerita>>()
    val komentarTerbanyakList: LiveData<List<ListBerita>> get() = _komentarTerbanyakList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadKomentarTerbanyak() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getKomentarTerbanyak()
        client.enqueue(object : Callback<ResponseGetBerita> {
            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
            }

            override fun onResponse(call: Call<ResponseGetBerita>, response:
            Response<ResponseGetBerita>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("KomentarTerbanyak", "Response: ${response.body()?.result}")

                    _komentarTerbanyakList.value = response.body()?.result ?: emptyList()
                }
            }
        })
    }
}