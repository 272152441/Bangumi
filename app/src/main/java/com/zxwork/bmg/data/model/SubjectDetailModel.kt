package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SubjectDetailModel(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: Int,
    @SerialName("name")
    val name: String,
    @SerialName("name_cn")
    val nameCn: String,
    @SerialName("summary")
    val summary: String,
    @SerialName("date")
    val date: String? = null,
    @SerialName("platform")
    val platform: String? = null,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("infobox")
    val infobox: List<InfoBoxItem>? = null,
    @SerialName("rating")
    val rating: Rating? = null,
    @SerialName("collection")
    val collection: Collection? = null,
    @SerialName("tags")
    val tags: List<Tag>? = null
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
    data class InfoBoxItem(
        @SerialName("key")
        val key: String,
        @SerialName("value")
        val value: JsonElement
    )

    @Serializable
    data class Rating(
        @SerialName("score")
        val score: Double,
        @SerialName("total")
        val total: Int,
        @SerialName("rank")
        val rank: Int? = null
    )

    @Serializable
    data class Collection(
        @SerialName("wish")
        val wish: Int,
        @SerialName("collect")
        val collect: Int,
        @SerialName("doing")
        val doing: Int,
        @SerialName("on_hold")
        val onHold: Int,
        @SerialName("dropped")
        val dropped: Int
    )

    @Serializable
    data class Tag(
        @SerialName("name")
        val name: String,
        @SerialName("count")
        val count: Int
    )
}
