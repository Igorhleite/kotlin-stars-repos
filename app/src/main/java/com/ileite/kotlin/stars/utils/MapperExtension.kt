package com.ileite.kotlin.stars.utils

import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.data.model.RepositoriesDomain

fun RepositoriesDomain.fromDomainToModel(): GitRepositoryModel =
    GitRepositoryModel(
        id = this.id ?: 0,
        name = this.name ?: "",
        description = this.description ?: "",
        stars = this.stars ?: 0,
        forks = this.forks ?: 0,
        ownerName = this.owner.name ?: "",
        ownerPhotoUrl = this.owner.img ?: ""
    )

fun List<RepositoriesDomain>.fromDomainsToModels(): List<GitRepositoryModel> =
    this.map {
        it.fromDomainToModel()
    }