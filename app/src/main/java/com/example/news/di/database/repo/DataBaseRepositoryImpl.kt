package com.example.news.di.database.repo

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.AppRoomDatabase
import com.example.news.di.database.NewsDao
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.database.holders.toNews
import com.example.news.di.local.News

class DataBaseRepositoryImpl(private val appRoomDatabase: AppRoomDatabase) : DataBaseRepository {
    private val newsDao: NewsDao = appRoomDatabase.newsDao()

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

    override suspend fun clearCashedNewsAndAddNew(newsList: List<News>) {
        appRoomDatabase.withTransaction {
            val oldNews = newsDao.getCashedNews()
            oldNews.forEach { newsDao.removeAuthorsByArticleId(it.id) }
            newsDao.removeNews(oldNews)

            newsDao.addToNews(newsList.map { NewsEntity(it) })
            newsList.forEach { news ->
                news.author?.let { authors ->
                    val authorsEntityList = authors.map { AuthorEntity("", it, news.articalId) }
                    newsDao.addToFavoriteAuthor(authorsEntityList)
                }
            }
        }
    }

    override suspend fun getNewsById(newsId: String): Result<News, DataError.DatabaseError> {
        val newsEntity = newsDao.getNewsById(newsId)
            ?: return Result.ErrorResult(DataError.DatabaseError.NOT_FOUND)
        val authors = newsDao.getNewsAuthor(newsEntity.id)?.map { it.authorName }
        return Result.SuccessResult(News(newsEntity, authors))
    }

    override fun pagingSource(): PagingSource<Int, NewsEntity> {
        return newsDao.pagingSource()
    }

    override suspend fun getNewsList(): List<News> {
        return newsDao.getNewsHolder().map { it.toNews() }
//        return newsDao.getNewsList().map { newsEntity ->
//            val creators = newsDao.getNewsAuthor(newsEntity.id)
//            News(newsEntity, creators?.map { it.authorName })
//        }
    }
}