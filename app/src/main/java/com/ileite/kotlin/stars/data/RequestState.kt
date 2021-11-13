package com.ileite.kotlin.stars.data

import com.ileite.kotlin.stars.data.model.error.ResponseError

sealed class RequestState<out T> {
    data class ResponseSuccess<out R>(val data: R) : RequestState<R>()
    data class ResponseFailure<out R>(val error: ResponseError?) :
        RequestState<R>()

    data class ResponseException<out R>(val exception: Exception) : RequestState<R>()
}