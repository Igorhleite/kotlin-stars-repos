package com.ileite.kotlin.stars.utils

import com.ileite.kotlin.stars.data.model.GitRepositoryModel
import com.ileite.kotlin.stars.data.model.dto.RepositoriesDomain
import com.ileite.kotlin.stars.data.model.entities.GitRepositoryEntity

fun RepositoriesDomain?.fromDomainToModel(): GitRepositoryModel =
    GitRepositoryModel(
        id = this?.id.toString() ?: "",
        name = this?.name ?: "",
        description = this?.description ?: "",
        stars = this?.stars.toString() ?: "",
        forks = this?.forks.toString() ?: "",
        ownerName = this?.owner?.name ?: "",
        ownerPhotoUrl = this?.owner?.img ?: ""
    )

fun List<RepositoriesDomain>.fromDomainsToModels(): List<GitRepositoryModel> =
    this.map {
        it.fromDomainToModel()
    }

fun GitRepositoryModel.fromModelToEntity(): GitRepositoryEntity =
    GitRepositoryEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        stars = this.stars,
        forks = this.forks,
        ownerName = this.ownerName,
        ownerPhotoUrl = this.ownerPhotoUrl
    )

fun List<GitRepositoryModel>.fromModelsToEntities(): List<GitRepositoryEntity> =
    this.map {
        it.fromModelToEntity()
    }

fun GitRepositoryEntity.fromEntityToModel(): GitRepositoryModel =
    GitRepositoryModel(
        id = this.id,
        name = this.name,
        description = this.description,
        stars = this.stars,
        forks = this.forks,
        ownerName = this.ownerName,
        ownerPhotoUrl = this.ownerPhotoUrl
    )

fun List<GitRepositoryEntity>.fromEntitiesToModels(): List<GitRepositoryModel> =
    this.map {
        it.fromEntityToModel()
    }