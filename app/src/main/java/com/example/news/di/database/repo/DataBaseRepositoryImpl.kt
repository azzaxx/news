package com.example.news.di.database.repo

import com.example.news.di.database.NewsDao
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataBaseRepositoryImpl(private val newsDao: NewsDao) : DataBaseRepository {
    override suspend fun addToFavoriteNews(news: News) {
        newsDao.addToFavoriteNews(NewsEntity(news))
        news.author?.let { authors ->
            val authorsEntityList = authors.map { AuthorEntity("", it, news.articalId) }
            newsDao.addToFavoriteAuthor(authorsEntityList)
        }
    }

    override suspend fun removeFromFavoriteNews(news: News) {
        newsDao.removeFavoriteNews(NewsEntity(news))
        news.author?.let { authors ->
            val authorsEntityList = authors.map { AuthorEntity("", it, news.articalId) }
            newsDao.removeFavoriteAuthors(authorsEntityList)
        }
    }

    override fun getNewsList(): Flow<List<News>> {
        return newsDao.getNewsList()
            .map { listNewsEntity ->
                listNewsEntity.map { newsEntity ->
                    val creators = newsDao.getNewsAuthor(newsEntity.id)
                    News(newsEntity, creators?.map { it.authorName })
                }
            }
    }
}