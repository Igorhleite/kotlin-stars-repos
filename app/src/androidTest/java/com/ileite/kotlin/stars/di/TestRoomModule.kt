package com.ileite.kotlin.stars.di

import android.content.Context
import androidx.room.Room
import com.ileite.kotlin.stars.data.local.RepositoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestRoomModule {
    @Provides
    @Named("test_repositories_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, RepositoriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}