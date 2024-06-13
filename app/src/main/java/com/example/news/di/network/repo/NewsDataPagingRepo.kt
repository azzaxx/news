package com.example.news.di.network.repo

import androidx.paging.PagingData
import com.example.news.di.local.News
import kotlinx.coroutines.flow.Flow

interface NewsDataPagingRepo {
    fun getNewsDataList(): Flow<PagingData<News>>
}