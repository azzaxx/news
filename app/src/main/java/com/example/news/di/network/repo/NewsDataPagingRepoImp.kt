package com.example.news.di.network.repo

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.news.di.database.holders.NewsDataIoHolder
import com.example.news.di.database.holders.toNews
import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsDataPagingRepoImp @Inject constructor(
    private val pager: Pager<Int, NewsDataIoHolder>
): NewsDataPagingRepo {
    override fun getNewsDataList(): Flow<PagingData<News>> {
        return pager.flow.map { pagingData ->
            pagingData.map { it.toNews() }
        }
    }
}