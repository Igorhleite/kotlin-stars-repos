package com.ileite.kotlin.stars.data.model.dto

import com.google.gson.annotations.SerializedName

data class OwnerDomain(
    @SerializedName("login")
    val name: String?,
    @SerializedName("avatar_url")
    val img: String?,
)