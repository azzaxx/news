package com.example.news.di.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToNews(newsEntity: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavoriteAuthor(authors: List<AuthorEntity>)

    @Query("SELECT * FROM news_table")
    fun getNewsListFlow(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table ORDER by pub_date DESC")
    suspend fun getNewsList(): List<NewsEntity>

    @Query("SELECT * FROM news_table WHERE is_favorite == 0")
    suspend fun getCashedNews(): List<NewsEntity>

    @Query("SELECT * FROM author_table WHERE article_id =:articleId")
    suspend fun getNewsAuthor(articleId: String): List<AuthorEntity>?

    @Delete
    suspend fun removeNews(news: List<NewsEntity>)

    @Delete
    suspend fun removeAuthors(authors: List<AuthorEntity>)

    @Query("DELETE FROM author_table WHERE article_id =:articleId")
    suspend fun removeAuthorsByArticleId(articleId: String)

    @Query("SELECT * FROM news_table WHERE id =:articleId")
    suspend fun getNewsById(articleId: String) : NewsEntity?
}