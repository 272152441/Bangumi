package com.zxwork.bmg.domain.model

data class SubjectDetail(
    val id: Int,
    val type: Int,
    val name: String,
    val nameCn: String,
    val summary: String,
    val date: String,
    val platform: String,
    val images: Subject.Images?,
    val rating: Rating?,
    val collection: Collection?,
    val tags: List<Tag>,
    val infobox: List<InfoBoxItem>
) {
    data class Rating(
        val score: Double,
        val total: Int,
        val rank: Int?
    )

    data class Collection(
        val wish: Int,
        val collect: Int,
        val doing: Int,
        val onHold: Int,
        val dropped: Int
    )

    data class Tag(
        val name: String,
        val count: Int
    )

    data class InfoBoxItem(
        val key: String,
        val value: String
    )
}
