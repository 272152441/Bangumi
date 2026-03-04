package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectModel(
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
    @SerialName("nsfw")
    val nsfw: Boolean,
    @SerialName("locked")
    val locked: Boolean,
    @SerialName("date")
    val date: String? = null,
    @SerialName("platform")
    val platform: String? = null,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("volumes")
    val volumes: Int? = null,
    @SerialName("eps")
    val eps: Int? = null,
    @SerialName("total_episodes")
    val totalEpisodes: Int? = null,
    @SerialName("rating")
    val rating: Rating? = null,
    @SerialName("collection")
    val collection: Collection? = null
) {
    @Serializable
    data class Images(
        @SerialName("large")
        val large: String,
        @SerialName("common")
        val common: String,
        @SerialName("medium")
        val medium: String,
        @SerialName("small")
        val small: String,
        @SerialName("grid")
        val grid: String
    )

    @Serializable
    data class Rating(
        @SerialName("rank")
        val rank: Int,
        @SerialName("total")
        val total: Int,
        @SerialName("count")
        val count: Map<String, Int>,
        @SerialName("score")
        val score: Double
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
}
