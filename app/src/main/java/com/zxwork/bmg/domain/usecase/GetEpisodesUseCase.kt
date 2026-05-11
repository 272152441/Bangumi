package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.repository.SubjectRepository
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subjectId: Int): NetworkResult<List<Episode>> {
        return repository.getEpisodes(subjectId)
    }
}
