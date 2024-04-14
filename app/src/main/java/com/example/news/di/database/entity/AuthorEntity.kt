package com.example.news.di.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author_table")
data class AuthorEntity(
    @PrimaryKey
    @ColumnInfo
    val id: String,
    @ColumnInfo("author_name")
    val authorName: String,
    @ColumnInfo("article_id")
    val articleId: String
)