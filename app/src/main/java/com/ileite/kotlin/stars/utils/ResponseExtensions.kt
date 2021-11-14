package com.ileite.kotlin.stars.utils

import com.ileite.kotlin.stars.data.RequestState
import com.ileite.kotlin.stars.data.model.error.ResponseError
import retrofit2.Response


fun <T : Any> Response<T?>?.safeResponse(): RequestState<T?> {
    return try {
        when (this) {
            null -> RequestState.ResponseException(Exception("Response is null."))
            else -> when (isSuccessful) {
                true -> RequestState.ResponseSuccess(body())
                else -> RequestState.ResponseFailure(parseResponseError())
            }
        }
    } catch (e: Exception) {
        RequestState.ResponseException(e)
    }
}

fun <T> Response<T>.parseResponseError(): ResponseError {
    return ResponseError(code = this.code().toString(), message = this.errorBody()?.string() ?: "")
}