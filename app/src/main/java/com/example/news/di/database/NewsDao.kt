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
    suspend fun addToFavoriteNews(newsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavoriteAuthor(authors: List<AuthorEntity>)

    @Query("SELECT * FROM news_table")
    fun getNewsList(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM author_table WHERE article_id =:articleId")
    fun getNewsAuthor(articleId: String): List<AuthorEntity>?

    @Delete
    suspend fun removeFavoriteNews(favorite: NewsEntity)

    @Delete
    suspend fun removeFavoriteAuthors(authors: List<AuthorEntity>)
}