package com.zxwork.bmg.domain.model

data class Episode(
    val id: Int,
    val sort: Double, // episode number/order
    val type: Int,
    val name: String,
    val nameCn: String,
    val airdate: String,
    val duration: String,
    val desc: String
)
