package com.example.news.di.network.repo

import android.util.Log
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.local.News
import com.example.news.di.network.NewsDataService
import retrofit2.HttpException

class NetworkRepositoryImpl(
    private val networkService: NewsDataService,
    private val dataBaseRepository: DataBaseRepository
) : NetworkRepository {

    override suspend fun fetchNews(): Result<List<News>, DataError.NetworkError> {
        return try {
            val newsDataIo = networkService.getNews()
            val news = newsDataIo.results.map { News(it) }

            try {
                dataBaseRepository.clearCashedNews()
                dataBaseRepository.addToNews(news)
            } catch (e: Exception) {
                e.message?.let { Log.d("News App", "Cash news error: $it") }
            }

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
        }
    }
}