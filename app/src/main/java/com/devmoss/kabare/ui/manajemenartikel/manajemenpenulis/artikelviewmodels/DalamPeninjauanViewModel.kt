package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikelviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Artikel
import com.devmoss.kabare.data.repository.ArticleRepository

class DalamPeninjauanViewModel : ViewModel() {

    private val _dalamPeninjauanLiveData = MutableLiveData<List<Artikel>>()
    val dalamPeninjauanLiveData: LiveData<List<Artikel>> get() = _dalamPeninjauanLiveData

    init {
        loadDalamPeninjauan()
    }

    private fun loadDalamPeninjauan() {
        // Mengambil data dalam peninjauan dari repository
        val data = ArticleRepository.getArticles().filter {
            it.status == com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN &&
        it.statusPeninjauan == com.devmoss.kabare.data.model.StatusPeninjauan.DIAJUKAN||
        it.statusPeninjauan == com.devmoss.kabare.data.model.StatusPeninjauan.DITOLAK||
        it.statusPeninjauan == com.devmoss.kabare.data.model.StatusPeninjauan.REVISI_BESAR||
        it.statusPeninjauan == com.devmoss.kabare.data.model.StatusPeninjauan.REVISI_KECIL}
        _dalamPeninjauanLiveData.value = data
    }
}
