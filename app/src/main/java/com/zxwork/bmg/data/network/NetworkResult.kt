package com.zxwork.bmg.data.network

import com.zxwork.bmg.core.NetworkResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException

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
