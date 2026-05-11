package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.repository.SubjectRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(subjectId: Int): NetworkResult<List<Character>> {
        return repository.getCharacters(subjectId)
    }
}
