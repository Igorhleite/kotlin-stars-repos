package com.ileite.kotlin.stars.data.remote.repository

import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.data.remote.RepositoriesService
import com.ileite.kotlin.stars.utils.fromDomainsToModels
import com.ileite.kotlin.stars.utils.safeResponse
import javax.inject.Inject

class GitRepositoriesRepositoryImpl
@Inject
constructor(
    private val api: RepositoriesService,
) : GitRepositoriesRepository {
    override suspend fun getRepositories(page: Int): RequestState<List<GitRepositoryModel>>? {
        return when (val response = api.getRepositories(page = page).safeResponse()) {
            is RequestState.ResponseSuccess -> response.data?.let { gitResponseDomain ->
                gitResponseDomain.repositories?.let { repositoriesList ->
                    RequestState.ResponseSuccess(repositoriesList.fromDomainsToModels()) }
            }
            is RequestState.ResponseFailure -> RequestState.ResponseFailure(response.error)
            is RequestState.ResponseException -> RequestState.ResponseException(response.exception)
        }
    }
}