package com.example.news.di.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.di.local.News

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("pub_date")
    val pubDate: String,
    @ColumnInfo("image_url")
    val imageUrl: String?,
    @ColumnInfo("source_icon")
    val sourceIcon: String?,
    @ColumnInfo("source")
    val source: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Int,
    @ColumnInfo("news_data_next_page")
    val newsDataNextPage: String? = null
) {
    constructor(news: News) : this(
        id = news.articalId,
        title = news.title,
        description = news.description,
        content = news.content,
        pubDate = news.pubDate,
        imageUrl = news.imageUrl,
        sourceIcon = news.sourceIcon,
        source = news.source,
        isFavorite = if (news.isFavorite) 1 else 0,
        newsDataNextPage = news.nextPage
    )
}