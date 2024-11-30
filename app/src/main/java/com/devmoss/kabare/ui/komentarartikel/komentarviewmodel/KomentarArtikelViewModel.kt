package com.devmoss.kabare.ui.komentarartikel.komentarviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.KomentarArtikel
import com.devmoss.kabare.data.repository.KomentarRepository

class KomentarArtikelViewModel : ViewModel() {

    private val _comments = MutableLiveData<List<KomentarArtikel>>()
    val comments: LiveData<List<KomentarArtikel>> get() = _comments

    // Fungsi untuk memuat komentar berdasarkan ID artikel tertentu
    fun loadComments(articleId: Int) {
        val comments = KomentarRepository.getCommentsByArticleId(articleId)
        Log.d("KomentarArtikelViewModel", "Comments for article ID $articleId: $comments")
        _comments.value = comments
    }



    // Fungsi opsional untuk menambahkan komentar baru
    fun addComment(comment: KomentarArtikel) {
        KomentarRepository.addComment(comment)
        // Update LiveData setelah menambahkan komentar baru
        loadComments(comment.artikelId) // Memuat ulang komentar berdasarkan ID artikel
    }
    // Fungsi untuk menghapus komentar
    fun hapusKomentar(komentar: KomentarArtikel) {
        KomentarRepository.removeComment(komentar.komentarid) // Menghapus komentar dari repository
        loadComments(komentar.artikelId) // Memuat ulang komentar setelah penghapusan
        Log.d("KomentarArtikelViewModel", "Komentar dihapus: ${komentar.komentarid}")
    }
}
