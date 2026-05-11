package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.SubjectDetail
import com.zxwork.bmg.domain.repository.SubjectRepository
import javax.inject.Inject

class GetSubjectDetailUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(id: Int): NetworkResult<SubjectDetail> {
        return repository.getSubjectDetail(id)
    }
}
