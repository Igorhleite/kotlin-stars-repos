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
/**
 * By IgorHLeite on 13/11/2021
 *
 * This class provides the implementation of Remote Mediator, a feature of Paging 3.
 *
 * @getRepositories this variable provides access to UseCase that get remote data.
 * @db this variable provides access to the local database
 */
class RepositoriesRemoteMediator(
    private val getRepositories: GetRepositories,
    private val db: RepositoriesDatabase,
) : RemoteMediator<Int, GitRepositoryEntity>() {

    /**
     * This function runs to completion before any loads are performed.
     */
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    /**
     * Callback triggered when Paging needs to request more data from a remote source, your responsibility is to make the remote call and save the data locally.
     *
     * @loadType this variable provides LoadType of the condition that triggered this callback (PREPEND,APPEND,REFRESH).
     * @state this variable provides a copy of the state including a list of pages currently held in PagingData's memory.
     */
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
                db.repositoriesDao()
                    .setAllRepositories(repositories = repositories.fromModelsToEntities())
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
            /**
             * REFRESH indicates that an update was requested.
             * This usually means that a request to load remote data and replace all local data has been made.
             */
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.PAGE_INDEX
            }
            /**
             * Indicates the end of paging in the APPEND (next page) direction has been reached.
             * This occurs when PagingSource.load returns a LoadResult.Page with LoadResult.Page.nextKey == null.
             */
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            /**
             * Indicates that the end of paging in the PREPEND (prev page) direction has been reached.
             * This occurs when PagingSource.load returns a LoadResult.Page with LoadResult.Page.prevKey == null.
             */
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                return remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    /**
     * Get the closest remote key inserted which had the data.
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeyDao().getRemoteKey(repoId)
            }
        }
    }

    /**
     * Get the last remote key inserted which had the data.
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { user -> db.remoteKeyDao().getRemoteKey(user.id) }
    }

    /**
     * Get the first remote key inserted which had the data.
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, GitRepositoryEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { user -> db.remoteKeyDao().getRemoteKey(user.id) }
    }

}