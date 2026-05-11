package com.zxwork.bmg.domain.repository

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail

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
    ): NetworkResult<List<Subject>>

    suspend fun getSubjectDetail(id: Int): NetworkResult<SubjectDetail>

    suspend fun getEpisodes(subjectId: Int): NetworkResult<List<Episode>>

    suspend fun getCharacters(subjectId: Int): NetworkResult<List<Character>>

    suspend fun getLinkSubjects(subjectId: Int): NetworkResult<List<Subject>>
}
