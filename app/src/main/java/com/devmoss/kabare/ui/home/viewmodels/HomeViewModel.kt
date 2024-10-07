package com.devmoss.kabare.ui.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmoss.kabare.data.model.Article
import com.devmoss.kabare.data.repository.ArticleRepository

class HomeViewModel : ViewModel() {

    private val _sortedArticles = MutableLiveData<List<Article>>()
    val sortedArticles: LiveData<List<Article>> = _sortedArticles

    fun loadArticles() {
        val articles = ArticleRepository.getArticles()

        // Sorting by time (newest first) and popularity
        _sortedArticles.value = articles.sortedByDescending { it.timestamp }
    }

    fun toggleBookmark(article: Article) {
        article.isBookmarked = !article.isBookmarked
        _sortedArticles.value = _sortedArticles.value
    }
}
