package com.ileite.kotlin.stars.di

import com.ileite.kotlin.stars.data.remote.RepositoriesService
import com.ileite.kotlin.stars.data.remote.repository.GitRepositoriesRepository
import com.ileite.kotlin.stars.data.remote.repository.GitRepositoriesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRemoteUserRepository(
        picPayService: RepositoriesService,
    ): GitRepositoriesRepository {
        return GitRepositoriesRepositoryImpl(picPayService)
    }
}