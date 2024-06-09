package com.example.news.di.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.database.holders.NewsDataIoHolder
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Upsert
    suspend fun addToNews(newsEntity: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavoriteAuthor(authors: List<AuthorEntity>)

    @Query("SELECT * FROM news_table WHERE is_favorite == 0")
    suspend fun getCashedNews(): List<NewsDataIoHolder>

    @Delete
    suspend fun removeNews(news: List<NewsEntity>)

    @Delete
    suspend fun removeAuthors(authors: List<AuthorEntity>)

    @Query("DELETE FROM author_table")
    suspend fun removeAuthorsByArticleId(authors: List<AuthorEntity>)

    @Query("SELECT * FROM news_table WHERE id =:articleId")
    suspend fun getNewsById(articleId: String): NewsDataIoHolder?

    @Query("SELECT * FROM news_table")
    fun pagingSource(): PagingSource<Int, NewsEntity>

    @Transaction
    @Query("SELECT * FROM news_table")
    fun getNewsHolder() : Flow<List<NewsDataIoHolder>>
}