package com.zxwork.bmg.data.model

enum class SubjectGameCategory(val code: Int) {
    OTHER(0),
    GAME(4001),
    SOFTWARE(4002),
    EXPANSION(4003),
    BOARD_GAME(4005);
    companion object {
        fun fromCode(code: Int): SubjectGameCategory? = values().firstOrNull { it.code == code }
    }
}

