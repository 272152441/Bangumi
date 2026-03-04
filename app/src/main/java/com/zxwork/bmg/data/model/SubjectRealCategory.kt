package com.zxwork.bmg.data.model

enum class SubjectRealCategory(val code: Int) {
    OTHER(0),
    JP_DRAMA(1),
    WESTERN_DRAMA(2),
    CHINESE_DRAMA(3),
    DRAMA_SERIES(6001),
    FILM(6002),
    PERFORMANCE(6003),
    VARIETY(6004);
    companion object {
        fun fromCode(code: Int): SubjectRealCategory? = values().firstOrNull { it.code == code }
    }
}

