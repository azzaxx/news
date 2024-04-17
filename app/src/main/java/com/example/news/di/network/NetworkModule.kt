package com.example.news.di.network

import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.network.repo.NetworkRepository
import com.example.news.di.network.repo.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
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
    fun provideNetworkRepository(
        newsDataService: NewsDataService,
        dataBaseRepository: DataBaseRepository
    ): NetworkRepository {
        return NetworkRepositoryImpl(newsDataService, dataBaseRepository)
    }
}