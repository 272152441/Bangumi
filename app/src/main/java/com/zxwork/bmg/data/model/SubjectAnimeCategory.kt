package com.zxwork.bmg.data.model

enum class SubjectAnimeCategory(val code: Int) {
    OTHER(0),
    TV(1),
    OVA(2),
    MOVIE(3),
    WEB(5);
    companion object {
        fun fromCode(code: Int): SubjectAnimeCategory? = values().firstOrNull { it.code == code }
    }
}

