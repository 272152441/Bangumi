package com.zxwork.bmg.domain.usecase

import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectType
import com.zxwork.bmg.domain.repository.SubjectRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

data class SubjectSection(
    val type: SubjectType,
    val items: List<Subject>
)

class GetHomeSubjectsUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    suspend operator fun invoke(): NetworkResult<List<SubjectSection>> = coroutineScope {
        val types = listOf(
            SubjectType.BOOK to 1001, // COMIC
            SubjectType.ANIME to 1,    // TV
            SubjectType.MUSIC to 1,    // ALBUM
            SubjectType.GAME to 4001,  // GAME
            SubjectType.REAL to 1      // JP_DRAMA
        )

        val results = types.map { (type, cateCode) ->
            async {
                val result = repository.getSubjects(
                    type = type.code,
                    cat = cateCode,
                    limit = 10,
                    offset = 0,
                    year = 2026,
                    sort = "rank"
                )
                type to result
            }
        }.awaitAll()

        val sections = mutableListOf<SubjectSection>()
        var firstError: NetworkResult<Nothing>? = null

        for ((type, result) in results) {
            when (result) {
                is NetworkResult.Success -> {
                    sections.add(SubjectSection(type, result.data))
                }
                is NetworkResult.HttpError -> if (firstError == null) firstError = result
                is NetworkResult.NetworkError -> if (firstError == null) firstError = result
                is NetworkResult.UnknownError -> if (firstError == null) firstError = result
            }
        }

        if (sections.isNotEmpty()) {
            NetworkResult.Success(sections)
        } else {
            firstError ?: NetworkResult.HttpError(500, "Unknown error")
        }
    }
}
