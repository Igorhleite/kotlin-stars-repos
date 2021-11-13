package com.ileite.kotlin.stars.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitRepositoryModel(
    val id: Int,
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val ownerName: String,
    val ownerPhotoUrl: String,
) : Parcelable