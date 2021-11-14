package com.ileite.kotlin.stars.domain.remote

import com.ileite.kotlin.stars.TestUtils
import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.remote.repository.GitRepositoriesRepository
import com.ileite.kotlin.stars.utils.parseResponseError
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRepositoriesTest {

    @MockK
    private lateinit var repository: GitRepositoriesRepository

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var useCase: GetRepositories

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        useCase = GetRepositories(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `useCase should call the repository and get a successful answer`() = runBlockingTest {
        coEvery {
            repository.getRepositories(page = 1)
        } returns RequestState.ResponseSuccess(TestUtils.getRepositoryResponseMock())
        val actualValue = useCase.invoke(GetRepositories.Params(page = 1))
        assert(actualValue is RequestState.ResponseSuccess)
    }

    @Test
    fun `useCase should call the repository and get a failure answer`() = runBlockingTest {
        coEvery {
            repository.getRepositories(page = 1)
        } returns RequestState.ResponseFailure(TestUtils.getResponseError()?.parseResponseError())
        val actualValue = useCase.invoke(GetRepositories.Params(page = 1))
        assert(actualValue is RequestState.ResponseFailure)
    }

    @Test
    fun `useCase should call the repository and get a exception answer`() = runBlockingTest {
        coEvery {
            repository.getRepositories(page = 1)
        } returns RequestState.ResponseException(Exception())
        val actualValue = useCase.invoke(GetRepositories.Params(page = 1))
        assert(actualValue is RequestState.ResponseException)
    }
}