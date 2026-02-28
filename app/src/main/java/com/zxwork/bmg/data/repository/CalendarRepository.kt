package com.zxwork.bmg.data.repository

import com.zxwork.bmg.data.model.CalendarModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

interface CalendarRepository {
    suspend fun getCalendars(): List<CalendarModel>
}

class CalendarRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : CalendarRepository {
    override suspend fun getCalendars(): List<CalendarModel> {
        return client.get("calendar").body()
    }
}
