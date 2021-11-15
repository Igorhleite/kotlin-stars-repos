package com.ileite.kotlin.stars.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ileite.kotlin.stars.AndroidTestUtils.getData
import com.ileite.kotlin.stars.AndroidTestUtils.getEntityMockList
import com.ileite.kotlin.stars.data.local.dao.RepositoriesDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class RepositoriesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_repositories_db")
    lateinit var database: RepositoriesDatabase
    private lateinit var dao: RepositoriesDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.repositoriesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertOnDataBase() = runBlockingTest {
        dao.setAllRepositories(getEntityMockList())
    }

    @Test
    fun getListOnDataBase() = runBlockingTest {
        dao.setAllRepositories(getEntityMockList())
        val actualValue = dao.getAllRepositories().getData()
        assert(actualValue.isNotEmpty())
    }

    @Test
    fun insertAndDeleteListOnDataBase() = runBlockingTest {
        dao.setAllRepositories(getEntityMockList())
        dao.deleteAllRepositories()
        val actualValue = dao.getAllRepositories().getData()
        assert(actualValue.isEmpty())
    }
}