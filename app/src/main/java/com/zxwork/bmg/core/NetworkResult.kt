package com.zxwork.bmg.core

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class HttpError(val code: Int, val message: String? = null) : NetworkResult<Nothing>()
    data class NetworkError(val exception: Exception) : NetworkResult<Nothing>()
    data class UnknownError(val exception: Throwable) : NetworkResult<Nothing>()

    fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is HttpError -> this
            is NetworkError -> this
            is UnknownError -> this
        }
    }
}
