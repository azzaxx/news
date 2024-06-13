package com.example.news.di.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.news.Result
import com.example.news.di.database.holders.NewsDataIoHolder
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.network.repo.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class NewsDataRemoteMediator(
    private val database: DataBaseRepository,
    private val networkRepository: NetworkRepository
) : RemoteMediator<Int, NewsDataIoHolder>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsDataIoHolder>
    ): MediatorResult {
        return try {
            val loadPage = when (loadType) {
                LoadType.REFRESH -> {
                    withContext(Dispatchers.IO) {
                        database.clearCashedNewsAndAddNew(null)
                    }
                    null
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    lastItem.newsEntity.newsDataNextPage
                }
            }

            delay(1000L)
            when(val newsResult = networkRepository.fetchNews(page = loadPage)) {
                is Result.ErrorResult -> MediatorResult.Success(endOfPaginationReached = false)
                is Result.SuccessResult -> {
                    database.addToNews(newsResult.data)
                    MediatorResult.Success(endOfPaginationReached = newsResult.data.isEmpty())
                }
            }
        } catch (e: java.io.IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}