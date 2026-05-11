package com.zxwork.bmg.ui.detail

import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail

data class SubjectDetailState(
    val isLoading: Boolean = false,
    val subject: SubjectDetail? = null,
    val episodes: List<Episode> = emptyList(),
    val characters: List<Character> = emptyList(),
    val linkSubjects: List<Subject> = emptyList(),
    val error: String? = null
)

sealed interface SubjectDetailIntent {
    data class LoadDetail(val id: Int) : SubjectDetailIntent
    data object Refresh : SubjectDetailIntent
}
