package com.example.news.di.database.repo

import com.example.news.di.local.News

interface DataBaseRepository {
    suspend fun addToNews(news: News)

    suspend fun addToNews(newsList: List<News>)

    suspend fun getNewsList(): List<News>

    suspend fun removeFromFavoriteNews(news: News)

    suspend fun clearCashedNews()
}