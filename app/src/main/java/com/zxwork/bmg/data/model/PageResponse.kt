package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    @SerialName("data")
    val data: List<T>,
    @SerialName("total")
    val total: Int? = null,
    @SerialName("limit")
    val limit: Int? = null,
    @SerialName("offset")
    val offset: Int? = null
)

