package com.ileite.kotlin.stars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ileite.kotlin.stars.data.local.dao.RemoteKeyDao
import com.ileite.kotlin.stars.data.local.dao.RepositoriesDao
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity
import com.ileite.kotlin.stars.data.model.entities.RemoteKeyEntity

@Database(
    entities = [GitRepositoryEntity::class, RemoteKeyEntity::class],
    version = 1
)
abstract class RepositoriesDatabase: RoomDatabase() {

    abstract fun repositoriesDao(): RepositoriesDao

    abstract fun remoteKeyDao(): RemoteKeyDao

}