package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.domain.model.Calendar
import com.zxwork.bmg.domain.repository.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class GetHomeCalendarsUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(): List<Calendar> {
        val todayId = LocalDate.now().dayOfWeek.value
        val tomorrowId = if (todayId == 7) 1 else todayId + 1

        return repository.getCalendars()
            .filter { it.weekday.id == todayId || it.weekday.id == tomorrowId }
            .sortedBy { 
                // 保证今天在前
                if (it.weekday.id == todayId) 0 else 1
            }
    }
}
