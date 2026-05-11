package com.zxwork.bmg.domain.model

data class Calendar(
    val items: List<Item>,
    val weekday: Weekday
) {
    data class Item(
        val id: Int,
        val name: String,
        val nameCn: String,
        val airDate: String,
        val airWeekday: Int,
        val images: Images? = null,
        val score: Double? = null,
        val rank: Int? = null,
        val summary: String
    )

    data class Images(
        val common: String,
        val medium: String,
        val small: String,
        val grid: String,
        val large: String
    )

    data class Weekday(
        val id: Int,
        val cn: String,
        val en: String,
        val ja: String
    )
}
