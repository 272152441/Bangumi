package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    @SerialName("data")
    val data: List<T>? = null,
    @SerialName("total")
    val total: Int = 0,
    @SerialName("limit")
    val limit: Int = 0,
    @SerialName("offset")
    val offset: Int = 0
)

