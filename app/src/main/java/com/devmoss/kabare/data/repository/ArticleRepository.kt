package com.devmoss.kabare.data.repository

import com.devmoss.kabare.data.model.Article
import java.util.*

object ArticleRepository {

    // Simulated data source
    private val articles = mutableListOf<Article>()

    init {
        // Add some sample articles
        articles.add(
            Article(
                id = 1,
                imageUrl = "https://example.com/image1.jpg",
                title = "Breaking News: Important Event",
                timestamp = System.currentTimeMillis() - 60000, // 1 minute ago
                authorProfile = "John Doe",
                description = "This is a brief description of the important event that just happened.",
                isBookmarked = false
            )
        )
        articles.add(
            Article(
                id = 2,
                imageUrl = "https://example.com/image2.jpg",
                title = "Tech: New Innovations in AI",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "A new breakthrough in AI technology has been announced.",
                isBookmarked = false
            )
        )
        articles.add(
            Article(
                id = 3,
                imageUrl = "https://example.com/image3.jpg",
                title = "Sports: Championship Results",
                timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
                authorProfile = "Mike Johnson",
                description = "The championship game results are in, and it was a thrilling match!",
                isBookmarked = true
            )
        )
    }

    // Function to retrieve articles (sorted by timestamp or other criteria)
    fun getArticles(): List<Article> {
        return articles
    }

    // Function to toggle bookmark status of an article
    fun toggleBookmark(articleId: Int) {
        val article = articles.find { it.id == articleId }
        article?.let {
            it.isBookmarked = !it.isBookmarked
        }
    }

    // Add a new article to the repository (for testing or adding new content)
    fun addArticle(article: Article) {
        articles.add(article)
    }

    // Function to remove an article (optional)
    fun removeArticle(articleId: Int) {
        articles.removeAll { it.id == articleId }
    }
}
