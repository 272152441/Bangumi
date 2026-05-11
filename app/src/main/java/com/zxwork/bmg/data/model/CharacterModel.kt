package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterModel(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("relation")
    val relation: String,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("actors")
    val actors: List<ActorModel>? = null
) {
    @Serializable
    data class Images(
        @SerialName("large")
        val large: String? = null,
        @SerialName("common")
        val common: String? = null,
        @SerialName("medium")
        val medium: String? = null,
        @SerialName("small")
        val small: String? = null,
        @SerialName("grid")
        val grid: String? = null
    )

    @Serializable
    data class ActorModel(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("images")
        val images: Images? = null
    )
}
