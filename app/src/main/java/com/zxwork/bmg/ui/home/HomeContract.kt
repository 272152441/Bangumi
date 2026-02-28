package com.zxwork.bmg.ui.home

import com.zxwork.bmg.data.model.CalendarModel

data class HomeState(
    val isLoading: Boolean = false,
    val calendars: List<CalendarModel> = emptyList(),
    val error: String? = null
)

sealed interface HomeIntent {
    data object LoadCalendars : HomeIntent
    data class OnItemClick(val itemId: Int) : HomeIntent
}
