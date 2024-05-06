package com.example.news.di.database.holders

import androidx.room.Embedded
import androidx.room.Relation
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.local.News

data class NewsDataIoHolder(
    @Embedded
    val newsEntity: NewsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "article_id"
    )
    val authors: List<AuthorEntity>
)

fun NewsDataIoHolder.toNews() : News {
    return News(this.newsEntity, this.authors.map { it.authorName })
}