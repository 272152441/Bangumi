package com.zxwork.bmg.data.model

enum class SubjectType(val code: Int,val text:String) {
    BOOK(1,"书籍"),
    ANIME(2,"动画"),
    MUSIC(3,"音乐"),
    GAME(4,"游戏"),
    REAL(6,"三次元");

    companion object {
        fun fromCode(code: Int): SubjectType? = values().firstOrNull { it.code == code }
    }
}

