package com.devmoss.kabare.ui.home.populer.populerviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.ListBerita
import com.devmoss.kabare.data.model.ResponseGetBerita
import com.devmoss.kabare.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeritaPopulerViewModel : ViewModel() {

    private val _beritaPopulerList = MutableLiveData<List<ListBerita>>()
    val beritaPopulerList: LiveData<List<ListBerita>> get() = _beritaPopulerList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadBeritaPopuler() {

        _isLoading.value = true
        val client = ApiConfig.getApiService().getBeritaPopuler()
        client.enqueue(object : Callback<ResponseGetBerita> {
            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
            }

            override fun onResponse(call: Call<ResponseGetBerita>, response:
            Response<ResponseGetBerita>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _beritaPopulerList.value = response.body()?.result ?: emptyList()
                }
            }
        })
    }
}
