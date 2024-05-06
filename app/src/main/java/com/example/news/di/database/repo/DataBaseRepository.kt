package com.example.news.di.database.repo

import androidx.paging.PagingSource
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.local.News

interface DataBaseRepository {
    suspend fun addToNews(news: News)

    suspend fun addToNews(newsList: List<News>)

    suspend fun getNewsList(): List<News>

    suspend fun removeFromFavoriteNews(news: News)

    suspend fun clearCashedNewsAndAddNew(newsList: List<News>)

    suspend fun getNewsById(newsId: String): Result<News, DataError.DatabaseError>

    fun pagingSource() : PagingSource<Int, NewsEntity>
}