package com.example.news.di.local

import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.network.NewsDataContent

data class News(
    val articalId: String,
    val title: String,
    val author: List<String>?,
    val description: String?,
    val content: String,
    val pubDate: String,
    val imageUrl: String?,
    val sourceIcon: String?,
    val source: String,
    var isFavorite: Boolean = false,
    val nextPage: String?
) {

    constructor(newsDataContent: NewsDataContent, nextPage: String?) : this(
        articalId = newsDataContent.articleId,
        title = newsDataContent.title,
        author = newsDataContent.creator,
        description = newsDataContent.description,
        content = newsDataContent.content,
        pubDate = newsDataContent.pubDate,
        imageUrl = newsDataContent.imageUrl,
        sourceIcon = newsDataContent.sourceIcon,
        source = "News Data IO",
        nextPage = nextPage
    )

    constructor(newsEntity: NewsEntity, creators: List<String>?) : this(
        articalId = newsEntity.id,
        title = newsEntity.title,
        author = creators,
        description = newsEntity.description,
        content = newsEntity.content,
        pubDate = newsEntity.pubDate,
        imageUrl = newsEntity.imageUrl,
        sourceIcon = newsEntity.sourceIcon,
        source = newsEntity.source,
        isFavorite = newsEntity.isFavorite == 1,
        nextPage = newsEntity.newsDataNextPage
    )
}