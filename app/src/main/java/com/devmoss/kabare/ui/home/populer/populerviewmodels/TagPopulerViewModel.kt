package com.devmoss.kabare.ui.home.populer.populerviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.ResponseGetTag
import com.devmoss.kabare.data.model.Tag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TagPopulerViewModel : ViewModel() {

    private val _tagPopulerList = MutableLiveData<List<Tag>>()
    val tagPopulerList: LiveData<List<Tag>> get() = _tagPopulerList

    fun loadTagPopuler() {
        val client = ApiConfig.getApiService().getPopularTags()

        client.enqueue(object : Callback<ResponseGetTag> {
            override fun onResponse(
                call: Call<ResponseGetTag>,
                response: Response<ResponseGetTag>
            ) {
                if (response.isSuccessful) {
                    _tagPopulerList.value = response.body()?.result ?: emptyList()
                }
            }

            override fun onFailure(call: Call<ResponseGetTag>, t: Throwable) {
                // Handle failure (e.g., log error or show message)
            }
        })
    }
}
