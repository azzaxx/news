package com.example.news

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.news.di.database.entity.NewsEntity
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.network.repo.NetworkRepository
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class NewsDataRemoteMediator(
    private val database: DataBaseRepository,
    private val networkRepository: NetworkRepository
) : RemoteMediator<String, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, NewsEntity>
    ): MediatorResult {
        return try {
            val loadPage = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    lastItem.newsDataNextPage
                }
            }
            val news = networkRepository.fetchNews(page = loadPage)
            
            MediatorResult.Success(false)
        } catch (e: java.io.IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}