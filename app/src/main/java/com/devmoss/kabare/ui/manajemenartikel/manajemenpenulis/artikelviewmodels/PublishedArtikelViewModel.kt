package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikelviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.model.StatusArtikel
import com.devmoss.kabare.data.model.StatusPeninjauan
import com.devmoss.kabare.data.repository.ArticleRepository

class PublishedArtikelViewModel : ViewModel() { // Ganti ke ViewModel, bukan ViewHolder
    private val _publishedArtikelLiveData = MutableLiveData<List<Artikel>>() // Perbaikan penulisan
    val publishedArtikelLiveData: LiveData<List<Artikel>> get() = _publishedArtikelLiveData

    init {
        loadPublishedArtikel()
    }

    private fun loadPublishedArtikel() {
        // Mengambil data artikel yang sudah diterbitkan dari repository
        val data = ArticleRepository.getArticles().filter {
            it.status == StatusArtikel.DITERBITKAN &&  it.statusPeninjauan == StatusPeninjauan.PUBLISHED }
        _publishedArtikelLiveData.value = data
    }
}
