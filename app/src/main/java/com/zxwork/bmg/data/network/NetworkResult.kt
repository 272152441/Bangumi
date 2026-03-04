package com.zxwork.bmg.data.network

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import java.io.IOException

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class HttpError(
        val code: Int,
        val message: String? = null
    ) : NetworkResult<Nothing>()
    data class NetworkError(val exception: IOException) : NetworkResult<Nothing>()
    data class UnknownError(val throwable: Throwable) : NetworkResult<Nothing>()
}

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e: ClientRequestException) {
        NetworkResult.HttpError(e.response.status.value, e.message)
    } catch (e: ServerResponseException) {
        NetworkResult.HttpError(e.response.status.value, e.message)
    } catch (e: IOException) {
        NetworkResult.NetworkError(e)
    } catch (t: Throwable) {
        NetworkResult.UnknownError(t)
    }
}

