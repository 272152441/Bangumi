package com.zxwork.bmg.data.repository

import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.data.model.SubjectListModel
import com.zxwork.bmg.data.model.PageResponse
import com.zxwork.bmg.data.network.NetworkResult
import com.zxwork.bmg.data.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

interface SubjectRepository {
    suspend fun getSubject(subjectId: Int): SubjectModel
    suspend fun getSubjects(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): SubjectListModel
    suspend fun getSubjectResult(subjectId: Int): NetworkResult<SubjectModel>
    suspend fun getSubjectsPage(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): PageResponse<SubjectListModel.Item>
    suspend fun getSubjectsPageResult(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): NetworkResult<PageResponse<SubjectListModel.Item>>
}

class SubjectRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : SubjectRepository {
    override suspend fun getSubject(subjectId: Int): SubjectModel {
        return client.get("v0/subjects/$subjectId").body()
    }

    override suspend fun getSubjects(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): SubjectListModel {
        return client.get("v0/subjects") {
            parameter("type", type)
            parameter("cat", cat)
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getSubjectResult(subjectId: Int): NetworkResult<SubjectModel> {
        return safeApiCall { client.get("v0/subjects/$subjectId").body() }
    }

    override suspend fun getSubjectsPage(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): PageResponse<SubjectListModel.Item> {
        return client.get("v0/subjects") {
            parameter("type", type)
            parameter("cat", cat)
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    override suspend fun getSubjectsPageResult(
        type: Int,
        cat: Int,
        limit: Int,
        offset: Int
    ): NetworkResult<PageResponse<SubjectListModel.Item>> {
        return safeApiCall {
            getSubjectsPage(type, cat, limit, offset)
        }
    }
}
