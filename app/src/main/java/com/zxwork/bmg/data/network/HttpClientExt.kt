package com.zxwork.bmg.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

suspend inline fun <reified T> HttpClient.getResult(
    path: String,
    noinline builder: HttpRequestBuilder.() -> Unit = {}
): NetworkResult<T> {
    return safeApiCall { get(path) { builder() }.body() }
}

