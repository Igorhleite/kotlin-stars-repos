package com.ileite.kotlin.stars.data.model.dto

import com.google.gson.annotations.SerializedName

data class GitResponseDomain(
    @SerializedName("items")
    val repositories: List<RepositoriesDomain>?,
)