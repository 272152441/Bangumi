package com.zxwork.bmg.ui.home

import com.zxwork.bmg.data.model.CalendarModel
import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.data.model.SubjectType

data class HomeState(
    val isLoading: Boolean = false,
    val calendars: List<CalendarModel> = emptyList(),
    val error: String? = null,
    val subjectSections: List<SubjectSection> = emptyList(),
    val isLoadingSubjects: Boolean = false
)

sealed interface HomeIntent {
    data object LoadCalendars : HomeIntent
    data class OnItemClick(val itemId: Int) : HomeIntent
    data object LoadSubjects : HomeIntent
}

data class SubjectSection(
    val type: SubjectType,
    val items: List<SubjectModel.Item>
)
