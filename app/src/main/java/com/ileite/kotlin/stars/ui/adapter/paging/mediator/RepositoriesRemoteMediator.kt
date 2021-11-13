package com.ileite.kotlin.stars.ui.adapter.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.local.RepositoriesDatabase
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity
import com.ileite.kotlin.stars.data.model.entities.RemoteKeyEntity
import com.ileite.kotlin.stars.domain.remote.GetRepositories
import com.ileite.kotlin.stars.utils.Constants
import com.ileite.kotlin.stars.utils.fromModelsToEntities

@ExperimentalPagingApi
class RepositoriesRemoteMediator(
    private val getRepositories: GetRepositories,
    private val db: RepositoriesDatabase,
) : RemoteMediator<Int, GitRepositoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GitRepositoryEntity>,
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val repositories = mutableListOf<GitRepositoryModel>()

            when (val response = getRepositories.invoke(GetRepositories.Params(page))) {
                is RequestState.ResponseSuccess -> repositories.addAll(response.data)
                else -> {
                    return MediatorResult.Error(Exception("Error on loading"))
                }
            }
            val isEndOfList = repositories.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.repositoriesDao().deleteAllRepositories()
                    db.remoteKeyDao().deleteAllRemoteKeys()
                }
                val prevKey = if (page == Constants.PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = repositories.map {
                    RemoteKeyEntity(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.repositoriesDao().setAllRepositories(repositories = repositories.fromModelsToEntities())
                db.remoteKeyDao().setAllRemoteKeys(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, GitRepositoryEntity>,
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                return remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    // get the closest remote key inserted which had the data
    private suspend fun getClosestRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeyDao().getRemoteKey(repoId)
            }
        }
    }

    // get the last remote key inserted which had the data
    private suspend fun getLastRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { user -> db.remoteKeyDao().getRemoteKey(user.id) }
    }

    // get the first remote key inserted which had the data
    private suspend fun getFirstRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { user -> db.remoteKeyDao().getRemoteKey(user.id) }
    }

}