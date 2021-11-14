package com.ileite.kotlin.stars.data

import com.ileite.kotlin.stars.data.model.error.ResponseError

sealed class RequestState<out T> {
    data class ResponseSuccess<out R>(val data: R) : RequestState<R>()
    data class ResponseFailure(val error: ResponseError?) :
        RequestState<Nothing>()

    data class ResponseException(val exception: Exception) : RequestState<Nothing>()
}