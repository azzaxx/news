package com.example.news.di.network.repo

import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.local.News

interface NetworkRepository {
    suspend fun fetchNews(page: String? = null): Result<List<News>, DataError.NetworkError>
}