package com.ileite.kotlin.stars.data.model.dto

import com.google.gson.annotations.SerializedName

data class RepositoriesDomain(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("stargazers_count")
    val stars: Int?,
    @SerializedName("forks_count")
    val forks: Int?,
    @SerializedName("owner")
    val owner: OwnerDomain,
)