package com.zxwork.bmg.domain.repository.impl

import com.zxwork.bmg.data.mapper.toDomain
import com.zxwork.bmg.data.model.CalendarModel
import com.zxwork.bmg.domain.model.Calendar
import com.zxwork.bmg.domain.repository.CalendarRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : CalendarRepository {
    override suspend fun getCalendars(): List<Calendar> {
        val dtos: List<CalendarModel> = client.get("calendar").body()
        return dtos.map { it.toDomain() }
    }
}
