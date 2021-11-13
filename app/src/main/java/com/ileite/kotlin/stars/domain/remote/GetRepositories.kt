package com.ileite.kotlin.stars.domain.remote

import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.data.remote.repository.GitRepositoriesRepository
import com.ileite.kotlin.stars.domain.UseCase
import javax.inject.Inject

class GetRepositories @Inject constructor(
    private val repository: GitRepositoriesRepository,
) : UseCase.Params<RequestState<List<GitRepositoryModel>>?, GetRepositories.Params> {

    override suspend fun invoke(params: Params): RequestState<List<GitRepositoryModel>>? {
        return repository.getRepositories(page = params.page)
    }

    data class Params(
        val page: Int,
    )
}