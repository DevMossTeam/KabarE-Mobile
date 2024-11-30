package com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.reviewerviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.repository.ArticleRepository

class DalamPeninjauanReviewerViewModel : ViewModel(){
    private val _dalamPeninjauanReviewerLiveData = MutableLiveData<List<Artikel>>()
    val dalamPeninjauanReviewerLiveData: LiveData<List<Artikel>> get() = _dalamPeninjauanReviewerLiveData

    init {
        loadDalamPeninjauanReviewer()
    }

    private fun loadDalamPeninjauanReviewer() {
        // Mengambil data dalam peninjauan dari repository
        val data = ArticleRepository.getArticles().filter {
            it.status == com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN}
        _dalamPeninjauanReviewerLiveData.value = data
    }
}