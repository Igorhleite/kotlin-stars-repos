package com.ileite.kotlin.stars.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.ileite.kotlin.stars.TestUtils.getRepositoryResponseMock
import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.domain.mediator.GetMediatorData
import com.ileite.kotlin.stars.utils.fromModelsToEntities
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    lateinit var useCase: GetMediatorData

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        initMocks()
        homeViewModel = HomeViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initMocks() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `view model should call the useCase and passValue for liveData`() = runBlockingTest {
        coEvery { useCase.invoke() } returns flow {
            emit(PagingData.from(getRepositoryResponseMock().fromModelsToEntities()))
        }
        homeViewModel.getRepositoriesRemotely()
        homeViewModel.repositoriesEvent.observeForever { actualValue ->
            assert(actualValue is PagingData<GitRepositoryModel>)
        }
    }
}