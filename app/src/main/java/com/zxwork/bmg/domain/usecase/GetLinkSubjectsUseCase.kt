package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.repository.SubjectRepository
import javax.inject.Inject

class GetLinkSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subjectId: Int): NetworkResult<List<Subject>> {
        return repository.getLinkSubjects(subjectId)
    }
}
