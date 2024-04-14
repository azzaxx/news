package com.example.news.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.di.database.entity.AuthorEntity
import com.example.news.di.database.entity.NewsEntity

@Database(
    entities = [
        NewsEntity::class,
        AuthorEntity::class
    ],
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}