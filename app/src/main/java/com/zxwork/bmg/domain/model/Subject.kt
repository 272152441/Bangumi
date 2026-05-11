package com.zxwork.bmg.domain.model

data class Subject(
    val id: Int,
    val type: Int,
    val name: String,
    val nameCn: String,
    val relation: String,
    val summary: String,
    val date: String,
    val platform: String,
    val images: Images? = null
) {
    data class Images(
        val large: String,
        val common: String,
        val medium: String,
        val small: String,
        val grid: String
    )
}
