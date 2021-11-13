package com.ileite.kotlin.stars.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ileite.kotlin.stars.data.local.RepositoriesDatabase
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.domain.remote.GetRepositories
import com.ileite.kotlin.stars.ui.adapter.paging.mediator.RepositoriesRemoteMediator
import com.ileite.kotlin.stars.utils.Constants
import com.ileite.kotlin.stars.utils.fromEntityToModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel
@Inject constructor(
    private val getRepositories: GetRepositories,
    private val db: RepositoriesDatabase,
) : ViewModel() {

    private val _repositoriesEvent = MutableLiveData<PagingData<GitRepositoryModel>>()
    val repositoriesEvent get() = _repositoriesEvent

    init {
        getRepositoriesRemotely()
    }

    fun getRepositoriesRemotely() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    enablePlaceholders = false,
                ),
                remoteMediator = RepositoriesRemoteMediator(
                    getRepositories = getRepositories,
                    db = db
                ),
                pagingSourceFactory = { db.repositoriesDao().getAllRepositories() }
            ).flow.cachedIn(viewModelScope).collect {
                _repositoriesEvent.value = it.map { repositoryEntity ->
                    repositoryEntity.fromEntityToModel()
                }
            }
        }
    }
}