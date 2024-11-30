package com.devmoss.kabare.data.repository

import com.devmoss.kabare.data.model.KomentarArtikel

object KomentarRepository {
    private val komentar = mutableListOf<KomentarArtikel>()

    init {
        // Data dummy untuk artikel dengan id 1
        komentar.add(KomentarArtikel("1", "User1", "profile1.png", 1, "Great article!", System.currentTimeMillis())) // Timestamp sebagai Long
        komentar.add(KomentarArtikel("2", "User2", "profile2.png", 1, "Very informative.", System.currentTimeMillis()))

        // Data dummy tambahan untuk artikel dengan id 2
        komentar.add(KomentarArtikel("11", "User11", "profile11.png", 1, "Nice article on this topic.", System.currentTimeMillis()))
        komentar.add(KomentarArtikel("12", "User12", "profile12.png", 1, "Very well written!", System.currentTimeMillis()))
        komentar.add(KomentarArtikel("13", "User13", "profile13.png", 2, "I enjoyed reading this.", System.currentTimeMillis()))
    }

    // Fungsi untuk menambahkan komentar
    fun addComment(komentarArtikel: KomentarArtikel) {
        komentar.add(komentarArtikel)
    }

    // Fungsi untuk mengambil semua komentar
    fun getAllComments(): List<KomentarArtikel> {
        return komentar
    }

    // Fungsi untuk mengambil komentar berdasarkan ID artikel
    fun getCommentsByArticleId(articleId: Int): List<KomentarArtikel> {
        return komentar.filter { it.artikelId == articleId }
    }

    // Fungsi untuk menghapus komentar berdasarkan ID
    fun removeComment(commentId: String) {
        komentar.removeAll { it.komentarid == commentId }
    }
}
