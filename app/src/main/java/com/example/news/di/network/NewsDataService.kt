package com.example.news.di.network

import retrofit2.http.GET
import retrofit2.http.Query

const val NEWS_DATA_IO_URL =
    "https://newsdata.io/api/1/"

interface NewsDataService {
    @GET("news?apikey=pub_4185501f47adea5e7971c9bb539b597c7ccea&language=uk")
    suspend fun getNews(@Query("page") page: String? = null): NewsDataResponsePojo
}