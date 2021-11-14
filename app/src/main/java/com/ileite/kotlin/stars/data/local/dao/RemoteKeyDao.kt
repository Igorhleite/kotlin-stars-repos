package com.ileite.kotlin.stars.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ileite.kotlin.stars.data.model.entities.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setAllRemoteKeys(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE repositoriesId = :id")
    suspend fun getRemoteKey(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}
