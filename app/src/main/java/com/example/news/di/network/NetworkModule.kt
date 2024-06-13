package com.example.news.di.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.news.di.database.holders.NewsDataIoHolder
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.network.repo.NetworkRepository
import com.example.news.di.network.repo.NetworkRepositoryImpl
import com.example.news.di.network.repo.NewsDataPagingRepo
import com.example.news.di.network.repo.NewsDataPagingRepoImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetwork(): NewsDataService {
        return Retrofit
            .Builder()
            .baseUrl(NEWS_DATA_IO_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsDataService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        newsDataService: NewsDataService
    ): NetworkRepository {
        return NetworkRepositoryImpl(newsDataService)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePager(
        dataBaseRepository: DataBaseRepository,
        networkRepository: NetworkRepository
    ): Pager<Int, NewsDataIoHolder> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NewsDataRemoteMediator(dataBaseRepository, networkRepository),
            pagingSourceFactory = {
                dataBaseRepository.pagingSource()
            }
        )
    }

    @Provides
    @Singleton
    fun provideNewsDataPagingRepo(pager: Pager<Int, NewsDataIoHolder>): NewsDataPagingRepo {
        return NewsDataPagingRepoImp(pager)
    }
}