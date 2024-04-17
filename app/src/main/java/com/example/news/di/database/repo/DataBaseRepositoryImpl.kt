package com.example.news.di.database.repo

import android.util.Log
import com.example.news.di.database.NewsDao
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataBaseRepositoryImpl(private val newsDao: NewsDao) : DataBaseRepository {

    override suspend fun addToNews(newsList: List<News>) {
        newsDao.addToNews(newsList.map { NewsEntity(it) })
        newsList.forEach { news ->
            news.author?.let { authors ->
                val authorsEntityList = authors.map { AuthorEntity("", it, news.articalId) }
                newsDao.addToFavoriteAuthor(authorsEntityList)
            }
        }
    }

    override suspend fun addToNews(news: News) {
        addToNews(listOf(news))
    }

    override suspend fun removeFromFavoriteNews(news: News) {
        newsDao.removeNews(listOf(NewsEntity(news)))
        news.author?.let { authors ->
            val authorsEntityList = authors.map { AuthorEntity("", it, news.articalId) }
            newsDao.removeAuthors(authorsEntityList)
        }
    }

    override suspend fun clearCashedNews() {
        val oldNews = newsDao.getCashedNews()
        oldNews.forEach { newsDao.removeAuthorsByArticleId(it.id) }
        newsDao.removeNews(oldNews)
    }

    override suspend fun getNewsList(): List<News> {
        return newsDao.getNewsList().map { newsEntity ->
            val creators = newsDao.getNewsAuthor(newsEntity.id)
            News(newsEntity, creators?.map { it.authorName })
        }
    }
}