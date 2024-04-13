package com.example.news.di.local

import com.example.news.di.network.NewsDataContent

class News(
    val title: String,
    val author: List<String>?,
    val description: String?,
    val content: String,
    val pubDate: String,
    val imageUrl: String?,
    val sourceIcon: String?,
    val sourse: String
) {
    constructor(newsDataContent: NewsDataContent) : this(
        title = newsDataContent.title,
        author = newsDataContent.creator,
        description = newsDataContent.description,
        content = newsDataContent.content,
        pubDate = newsDataContent.pubDate,
        imageUrl = newsDataContent.imageUrl,
        sourceIcon = newsDataContent.sourceIcon,
        sourse = "News Data IO"
    )
}