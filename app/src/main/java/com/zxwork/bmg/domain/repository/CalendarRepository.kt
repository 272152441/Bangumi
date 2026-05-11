package com.zxwork.bmg.domain.repository

import com.zxwork.bmg.domain.model.Calendar

interface CalendarRepository {
    suspend fun getCalendars(): List<Calendar>
}
