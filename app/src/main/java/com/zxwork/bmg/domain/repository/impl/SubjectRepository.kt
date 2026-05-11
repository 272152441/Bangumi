package com.zxwork.bmg.domain.repository.impl

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.data.mapper.toDomain
import com.zxwork.bmg.data.model.PageResponse
import com.zxwork.bmg.data.model.SubjectDetailModel
import com.zxwork.bmg.data.model.EpisodeModel
import com.zxwork.bmg.data.model.CharacterModel
import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.data.network.getResult
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.repository.SubjectRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import javax.inject.Inject

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
    ): NetworkResult<List<Subject>> {
        return client.getResult<PageResponse<SubjectModel>>("v0/subjects") {
            parameter("type", type)
            parameter("cat", cat)
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort", sort)
            parameter("year", year)
            parameter("month", month)
            parameter("platform", platform)
        }.map { response ->
            response.data?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun getSubjectDetail(id: Int): NetworkResult<SubjectDetail> {
        return client.getResult<SubjectDetailModel>("v0/subjects/$id").map { it.toDomain() }
    }

    override suspend fun getEpisodes(subjectId: Int): NetworkResult<List<Episode>> {
        return client.getResult<PageResponse<EpisodeModel>>("v0/episodes") {
            parameter("subject_id", subjectId)
            parameter("limit", 100) 
        }.map { response ->
            response.data?.map { it.toDomain() }?.sortedBy { it.sort } ?: emptyList()
        }
    }

    override suspend fun getCharacters(subjectId: Int): NetworkResult<List<Character>> {
        return client.getResult<List<CharacterModel>>("v0/subjects/$subjectId/characters").map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getLinkSubjects(subjectId: Int): NetworkResult<List<Subject>> {
        return  client.getResult<List<SubjectModel>>("v0/subjects/$subjectId/subjects").map { item ->
            item.map { it.toDomain() }
        }
    }
}
