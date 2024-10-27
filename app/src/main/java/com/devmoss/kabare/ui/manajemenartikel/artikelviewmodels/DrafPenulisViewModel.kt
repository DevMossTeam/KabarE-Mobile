package com.devmoss.kabare.ui.manajemenartikel.artikelviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.repository.ArticleRepository

class DrafPenulisViewModel : ViewModel() {

    private val _drafPenulisLiveData = MutableLiveData<List<Artikel>>()
    val drafPenulisLiveData: LiveData<List<Artikel>> get() = _drafPenulisLiveData

    init {
        loadDrafPenulis()
    }

    private fun loadDrafPenulis() {
        // Mengambil data draf penulis dari repository
        val data = ArticleRepository.getArticles().filter {
            it.status == com.devmoss.kabare.data.model.StatusArtikel.DRAF }
        _drafPenulisLiveData.value = data
    }
}
