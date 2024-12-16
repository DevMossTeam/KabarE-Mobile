package com.devmoss.kabare.ui.search

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

class SearchViewModel  : ViewModel() {

    private val _searchList = MutableLiveData<List<ListBerita>>()
    val searchList: LiveData<List<ListBerita>> get() = _searchList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadSearch(katakunci: String? = null, kategori: String? = null, tag: String? = null) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchBerita(katakunci, kategori, tag)
        client.enqueue(object : Callback<ResponseGetBerita> {
            override fun onFailure(call: Call<ResponseGetBerita>, t: Throwable) {
                _isLoading.value = false
                Log.e("SearchViewModel", "onFailure: ${t.message}", t)
            }

            override fun onResponse(call: Call<ResponseGetBerita>, response: Response<ResponseGetBerita>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _searchList.value = body?.result ?: emptyList()
                } else {
                    Log.e("SearchViewModel", "Error response: ${response.errorBody()?.string()}")
                }
            }
        })
    }

}
