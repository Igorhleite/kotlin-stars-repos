package com.ileite.kotlin.stars.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity

@Dao
interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllRepositories(repositories: List<GitRepositoryEntity>)

    @Query("SELECT * FROM repositories")
    fun getAllRepositories(): PagingSource<Int, GitRepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun deleteAllRepositories()
}