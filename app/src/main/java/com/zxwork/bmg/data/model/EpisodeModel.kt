package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeModel(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: Int,
    @SerialName("sort")
    val sort: Double,
    @SerialName("name")
    val name: String,
    @SerialName("name_cn")
    val nameCn: String,
    @SerialName("duration")
    val duration: String? = null,
    @SerialName("airdate")
    val airdate: String? = null,
    @SerialName("desc")
    val desc: String? = null
)
