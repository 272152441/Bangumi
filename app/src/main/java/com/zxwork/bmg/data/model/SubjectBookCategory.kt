package com.zxwork.bmg.data.model

enum class SubjectBookCategory(val code: Int) {
    OTHER(0),
    COMIC(1001),
    NOVEL(1002),
    ARTBOOK(1003);

    companion object {
        fun fromCode(code: Int): SubjectBookCategory? = values().firstOrNull { it.code == code }
    }
}

