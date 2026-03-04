package com.zxwork.bmg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SubjectListModel(
    @SerialName("data")
    val data: List<Item>
) {
    @Serializable
    data class Item(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("type")
        val type: Int? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("name_cn")
        val nameCn: String? = null,
        @SerialName("summary")
        val summary: String? = null,
        @SerialName("date")
        val date: String? = null,
        @SerialName("platform")
        val platform: String? = null,
        @SerialName("images")
        val images: Images? = null,
        @SerialName("tags")
        val tags: List<Tag>? = null,
        @SerialName("infobox")
        val infobox: List<InfoBoxItem>? = null
    )

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
    data class Tag(
        @SerialName("name")
        val name: String,
        @SerialName("count")
        val count: Int? = null,
        @SerialName("total_cont")
        val totalCont: Int? = null
    )

    @Serializable
    data class InfoBoxItem(
        @SerialName("key")
        val key: String,
        @SerialName("value")
        val value: JsonElement
    )
}

