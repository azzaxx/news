package com.example.news.di.database

import android.content.Context
import androidx.room.Room
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.database.repo.DataBaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return Room
            .databaseBuilder(context, AppRoomDatabase::class.java, "news_database")
            .build()
    }

    @Provides
    fun provideDatabaseRepository(appRoomDatabase: AppRoomDatabase) : DataBaseRepository {
        return DataBaseRepositoryImpl(appRoomDatabase)
    }
}