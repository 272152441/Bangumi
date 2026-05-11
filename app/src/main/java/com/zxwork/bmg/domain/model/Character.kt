package com.zxwork.bmg.domain.model

data class Character(
    val id: Int,
    val name: String,
    val relation: String, // 身份 (e.g. 主角, 配角)
    val images: Images?,
    val actors: List<Actor>
) {
    data class Images(
        val large: String,
        val common: String,
        val medium: String,
        val small: String,
        val grid: String
    )

    data class Actor(
        val id: Int,
        val name: String,
        val images: Images?
    )
}
