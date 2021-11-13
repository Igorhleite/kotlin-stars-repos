package com.ileite.kotlin.stars.di

import com.ileite.kotlin.stars.data.remote.RepositoriesService
import com.ileite.kotlin.stars.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideUserApi(
        retrofit: Retrofit.Builder
    ): RepositoriesService {
        return retrofit
            .build()
            .create(RepositoriesService::class.java)
    }
}