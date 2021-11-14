package com.ileite.kotlin.stars.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "repositoriesId")
    val repositoriesId: String,
    @ColumnInfo(name = "prevKey")
    val prevKey: Int?,
    @ColumnInfo(name = "nextKey")
    val nextKey: Int?,
)