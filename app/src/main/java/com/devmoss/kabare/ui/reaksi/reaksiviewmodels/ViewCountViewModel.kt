package com.devmoss.kabare.ui.reaksi.reaksiviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResponseViewCount
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewCountViewModel : ViewModel() {

    private val _viewCountResponse = MutableLiveData<ResponseViewCount?>()
    val viewCountResponse: LiveData<ResponseViewCount?> = _viewCountResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun incrementViewCount(beritaId: String) {
        viewModelScope.launch {
            val client = ApiConfig.getApiService().incrementViewCount(beritaId)
            client.enqueue(object : Callback<ResponseViewCount> {
                override fun onResponse(
                    call: Call<ResponseViewCount>,
                    response: Response<ResponseViewCount>
                ) {
                    if (response.isSuccessful) {
                        _viewCountResponse.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Terjadi kesalahan: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseViewCount>, t: Throwable) {
                    _errorMessage.postValue("Gagal menghubungi server: ${t.message}")
                }
            })
        }
    }
}
