package com.devmoss.kabare.ui.home.homeviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.repository.ArticleRepository
import com.devmoss.kabare.data.model.StatusArtikel // Asumsikan StatusArtikel adalah enum atau kelas yang digunakan untuk status artikel
import com.devmoss.kabare.data.model.StatusPeninjauan // Asumsikan StatusPeninjauan adalah enum atau kelas yang digunakan untuk status peninjauan

class HomeTerbaruViewModel : ViewModel() {

    private val _sortedArticles = MutableLiveData<List<Artikel>>()
    val sortedArticles: LiveData<List<Artikel>> = _sortedArticles

    private val _terkiniArticles = MutableLiveData<List<Artikel>>()
    val terkiniArticles: LiveData<List<Artikel>> = _terkiniArticles

    private val _teratasArticles = MutableLiveData<List<Artikel>>()
    val teratasArticles: LiveData<List<Artikel>> = _teratasArticles

    fun loadArticles() {
        val artikel = ArticleRepository.getArticles()

        // Filter artikel berdasarkan status diterbitkan dan status peninjauan diterima
        val publishedArticles = artikel.filter {
            it.status == StatusArtikel.DITERBITKAN && it.statusPeninjauan == StatusPeninjauan.PUBLISHED
        }

        // Sorting by time (newest first)
        val sortedByTime = publishedArticles.sortedByDescending { it.timestamp }

        // Pisahkan artikel menjadi berita terkini dan teratas
        _terkiniArticles.value = sortedByTime.filter { it.isTerkini }
        _teratasArticles.value = sortedByTime.filter { it.isTeratas }

        // Gabungkan ke dalam sortedArticles jika diperlukan
        _sortedArticles.value = sortedByTime
    }

    fun toggleBookmark(artikel: Artikel) {
        // Cek artikel yang sesuai dengan ID
        _sortedArticles.value = _sortedArticles.value?.map {
            if (it.id == artikel.id) {
                // Ubah status bookmark
                it.copy(isBookmarked = !it.isBookmarked)
            } else {
                it
            }
        }
    }
}
