package com.ileite.kotlin.stars.data.remote.repository

import com.ileite.kotlin.stars.TestUtils.getResponseError
import com.ileite.kotlin.stars.TestUtils.getResponseMock
import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.remote.RepositoriesService
import com.ileite.kotlin.stars.utils.fromDomainsToModels
import com.ileite.kotlin.stars.utils.parseResponseError
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class GitRepositoriesRepositoryTest {
    @MockK
    private lateinit var repositoriesService: RepositoriesService

    private lateinit var repository: GitRepositoriesRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        repository = GitRepositoriesRepositoryImpl(repositoriesService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `repository should call the service and get a successful response`() = runBlockingTest {
        coEvery { repositoriesService.getRepositories(page = 1) } returns Response.success(
            getResponseMock())
        val expectedValue =
            RequestState.ResponseSuccess(getResponseMock().repositories?.fromDomainsToModels())
        val actualValue = repository.getRepositories(page = 1)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `repository should call the service and get a error response`() = runBlockingTest {
        coEvery { repositoriesService.getRepositories(page = 1) } returns getResponseError()
        val expectedValue = RequestState.ResponseFailure(getResponseError()?.parseResponseError())
        val actualValue = repository.getRepositories(page = 1)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `repository should call the service and get a exception response`() = runBlockingTest {
        coEvery { repositoriesService.getRepositories(page = 1) } returns null
        val actualValue = repository.getRepositories(page = 1)
        assert(actualValue is RequestState.ResponseException)
    }
}