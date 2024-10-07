package com.devmoss.kabare.data.model

data class Article(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val timestamp: Long,
    val authorProfile: String,
    val description: String,
    var isBookmarked: Boolean
)
