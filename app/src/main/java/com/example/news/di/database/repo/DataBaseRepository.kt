package com.example.news.di.database.repo

import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun addToFavoriteNews(news: News)
    fun getNewsList(): Flow<List<News>>
    suspend fun removeFromFavoriteNews(news: News)
}