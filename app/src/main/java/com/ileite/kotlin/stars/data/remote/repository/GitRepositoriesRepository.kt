package com.ileite.kotlin.stars.data.remote.repository

import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.model.GitRepositoryModel

interface GitRepositoriesRepository {
    suspend fun getRepositories(page: Int): RequestState<List<GitRepositoryModel>?>
}