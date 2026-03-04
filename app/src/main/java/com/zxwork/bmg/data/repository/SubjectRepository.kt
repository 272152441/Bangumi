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
        offset: Int
    ): NetworkResult<PageResponse<SubjectModel.Item>>

}

class SubjectRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : SubjectRepository {

    override suspend fun getSubjects(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): NetworkResult<PageResponse<SubjectModel.Item>> {
        return client.getResult("v0/subjects") {
            parameter("type", type)
            parameter("cat", cat)
            parameter("limit", limit)
            parameter("offset", offset)
        }
    }
}
