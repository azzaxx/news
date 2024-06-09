package com.example.news.di.network.repo

import android.util.Log
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.local.News
import com.example.news.di.network.NewsDataService
import retrofit2.HttpException

class NetworkRepositoryImpl(
    private val networkService: NewsDataService
) : NetworkRepository {

    override suspend fun fetchNews(page: String?): Result<List<News>, DataError.NetworkError> {
        return try {
            val newsDataIo = networkService.getNews(page)
            val nextPage = newsDataIo.nextPage
            val news = newsDataIo.results.map { News(it, nextPage) }

            Result.SuccessResult(news)
        } catch (e: HttpException) {
            Log.d("News App", "Network call error: ${e.message()}")
            when (e.code()) {
                408 -> Result.ErrorResult(DataError.NetworkError.REQUEST_TIME_OUT)
                500 -> Result.ErrorResult(DataError.NetworkError.SERVER_DOWN)
                else -> Result.ErrorResult(DataError.NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            e.message?.let { Log.d("News App", "Network call error: $it") }
            Result.ErrorResult(DataError.NetworkError.UNKNOWN)
        } catch (e: Exception) {
            e.message?.let { Log.d("News App", "Network call error: $it") }
            Result.ErrorResult(DataError.NetworkError.UNKNOWN)
        }
    }
}