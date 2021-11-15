package com.ileite.kotlin.stars

import androidx.paging.PagingSource
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

object AndroidTestUtils {

    fun getEntityMockList(): List<GitRepositoryEntity> {
        val repoList = mutableListOf<GitRepositoryEntity>()
        for (i in 1..4) {
            repoList.add(
                GitRepositoryEntity(
                    id = "id$i",
                    name = "name$i",
                    repoUrl = "repoUrl$i",
                    stars = "stars$i",
                    forks = "forks$i",
                    ownerName = "ownerName$i",
                    ownerPhotoUrl = "ownerPhotoUrl$i"
                )
            )
        }
        return repoList
    }

    fun <PaginationKey: Any, Model: Any> PagingSource<PaginationKey, Model>.getData(): List<Model> {
        val data = mutableListOf<Model>()
        val latch = CountDownLatch(1)
        val job = CoroutineScope(Dispatchers.Main).launch {
            val loadResult: PagingSource.LoadResult<PaginationKey, Model> = this@getData.load(
                PagingSource.LoadParams.Refresh(
                    key = null, loadSize = Int.MAX_VALUE, placeholdersEnabled = false
                )
            )
            when (loadResult) {
                is PagingSource.LoadResult.Error -> throw loadResult.throwable
                is PagingSource.LoadResult.Page -> data.addAll(loadResult.data)
            }
            latch.countDown()
        }
        latch.await()
        job.cancel()
        return data
    }
}