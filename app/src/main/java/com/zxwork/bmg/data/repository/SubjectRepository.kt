package com.zxwork.bmg.data.repository

import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.data.model.PageResponse
import com.zxwork.bmg.data.network.NetworkResult
import com.zxwork.bmg.data.network.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import javax.inject.Inject

interface SubjectRepository {

    suspend fun getSubjects(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int,
        sort: String? = null,
        year: Int? = null,
        month: Int? = null,
        platform: String? = null,
        series: Boolean? = null,
    ): NetworkResult<PageResponse<SubjectModel.Item>>

}

class SubjectRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : SubjectRepository {

    override suspend fun getSubjects(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int,
        sort: String?,
        year: Int?,
        month: Int?,
        platform: String?,
        series: Boolean?
    ): NetworkResult<PageResponse<SubjectModel.Item>> {
        return client.getResult("v0/subjects") {
            parameter("type", type)
            parameter("cat", cat)
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort", sort)
            parameter("year", year)
            parameter("month", month)
            parameter("platform", platform)
        }
    }
}
