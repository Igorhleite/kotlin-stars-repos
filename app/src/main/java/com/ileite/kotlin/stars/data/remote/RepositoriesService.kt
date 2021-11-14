package com.ileite.kotlin.stars.data.remote

import com.ileite.kotlin.stars.data.model.dto.GitResponseDomain
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriesService {
    @GET("repositories")
    suspend fun getRepositories(
        @Query("q") q: String = "language:kotlin",
        @Query("sort") sort: String = "stars",
        @Query(value = "page") page: Int,
    ): Response<GitResponseDomain?>?
}