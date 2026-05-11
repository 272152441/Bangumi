package com.zxwork.bmg.data.model

enum class SubjectMusicCategory(val code: Int) {
    OTHER(0),
    ALBUM(1),
    SINGLE(2),
    EP(3);

    companion object {
        fun fromCode(code: Int): SubjectMusicCategory? = entries.firstOrNull { it.code == code }
    }
}
