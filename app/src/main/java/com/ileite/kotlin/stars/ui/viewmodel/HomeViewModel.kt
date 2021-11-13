package com.ileite.kotlin.stars.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.domain.remote.GetRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRepositories: GetRepositories,
) : ViewModel() {

    private val _repositoriesEvent = MutableLiveData<RequestState<List<GitRepositoryModel>>?>()
    val repositoriesEvent get() = _repositoriesEvent

    init {
        getRepositoriesRemotely()
    }

    fun getRepositoriesRemotely() {
        viewModelScope.launch {
            _repositoriesEvent.value = getRepositories.invoke(GetRepositories.Params(1))
        }
    }
}