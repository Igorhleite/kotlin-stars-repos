package com.ileite.kotlin.stars.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ileite.kotlin.stars.data.local.dao.RemoteKeyDao
import com.ileite.kotlin.stars.data.model.entities.RemoteKeyEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
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
class RemoteKeyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_repositories_db")
    lateinit var database: RepositoriesDatabase
    private lateinit var dao: RemoteKeyDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.remoteKeyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertOnDataBase() = runBlockingTest {
        dao.setAllRemoteKeys(listOf(RemoteKeyEntity("1", null, 2)))
    }

    @Test
    fun getKeyEntityOnDataBase() = runBlockingTest {
        val keyEntity = RemoteKeyEntity("1", null, 2)
        dao.setAllRemoteKeys(listOf(keyEntity))
        val actualValue = dao.getRemoteKey("1")
        assertEquals(keyEntity.repositoriesId, actualValue?.repositoriesId)
    }

    @Test
    fun insertAndDeleteKeyEntityOnDataBase() = runBlockingTest {
        val keyEntity = RemoteKeyEntity("1", null, 2)
        dao.setAllRemoteKeys(listOf(keyEntity))
        dao.deleteAllRemoteKeys()
        val actualValue = dao.getRemoteKey("1")
        assert(actualValue == null)
    }
}