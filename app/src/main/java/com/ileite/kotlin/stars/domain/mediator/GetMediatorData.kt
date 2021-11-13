package com.ileite.kotlin.stars.domain.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ileite.kotlin.stars.data.local.RepositoriesDatabase
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity
import com.ileite.kotlin.stars.domain.UseCase
import com.ileite.kotlin.stars.domain.remote.GetRepositories
import com.ileite.kotlin.stars.ui.adapter.paging.mediator.RepositoriesRemoteMediator
import com.ileite.kotlin.stars.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class GetMediatorData
@Inject
constructor(
    private val getRepositories: GetRepositories,
    private val db: RepositoriesDatabase,
) : UseCase.Empty<Flow<PagingData<GitRepositoryEntity>>> {

    override suspend fun invoke(): Flow<PagingData<GitRepositoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = RepositoriesRemoteMediator(
                getRepositories = getRepositories,
                db = db
            ),
            pagingSourceFactory = { db.repositoriesDao().getAllRepositories() }
        ).flow
    }
}
