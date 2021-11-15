package com.ileite.kotlin.stars.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitRepositoryModel(
    val id: String,
    val name: String,
    val repoUrl: String,
    val stars: String,
    val forks: String,
    val ownerName: String,
    val ownerPhotoUrl: String,
) : Parcelable