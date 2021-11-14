package com.ileite.kotlin.stars.data.model.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseError(
    var code: String = "",
    var message: String = "",
) : Parcelable