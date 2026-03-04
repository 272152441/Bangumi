package com.zxwork.bmg.data.model

enum class SubjectType(val code: Int) {
    BOOK(1),
    ANIME(2),
    MUSIC(3),
    GAME(4),
    REAL(6);

    companion object {
        fun fromCode(code: Int): SubjectType? = values().firstOrNull { it.code == code }
    }
}

