package com.example.news.di.database.repo

import androidx.paging.PagingSource
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.database.holders.NewsDataIoHolder
import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun addToNews(news: News)

    suspend fun addToNews(newsList: List<News>)

    fun getNewsList(): Flow<List<News>>

    suspend fun removeFromFavoriteNews(news: News)

    suspend fun clearCashedNewsAndAddNew(newsList: List<News>?)

    suspend fun getNewsById(newsId: String): Result<News, DataError.DatabaseError>

    fun pagingSource(): PagingSource<Int, NewsDataIoHolder>
}