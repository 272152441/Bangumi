package com.zxwork.bmg.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxwork.bmg.data.model.SortType
import com.zxwork.bmg.data.model.SubjectAnimeCategory
import com.zxwork.bmg.data.model.SubjectBookCategory
import com.zxwork.bmg.data.model.SubjectGameCategory
import com.zxwork.bmg.data.model.SubjectRealCategory
import com.zxwork.bmg.data.model.SubjectType
import com.zxwork.bmg.data.network.NetworkResult
import com.zxwork.bmg.data.repository.CalendarRepository
import com.zxwork.bmg.data.repository.SubjectRepository
import com.zxwork.bmg.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        onIntent(HomeIntent.LoadCalendars)
        onIntent(HomeIntent.LoadSubjects)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadCalendars -> loadCalendars()
            is HomeIntent.LoadSubjects -> loadSubjects()
            is HomeIntent.OnItemClick -> {
                // Handle item click if needed
            }
        }
    }

    private fun loadCalendars() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val todayId = LocalDate.now().dayOfWeek.value
                val tomorrowId = if (todayId == 7) 1 else todayId + 1

                val calendars = calendarRepository.getCalendars().filter { 
                    it.weekday.id == todayId || it.weekday.id == tomorrowId 
                }.sortedBy { 
                    // 保证今天在前
                    if (it.weekday.id == todayId) 0 else 1
                }
                
                _uiState.update { it.copy(isLoading = false, calendars = calendars) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSubjects = true) }
            val types = listOf(
                SubjectType.BOOK,
                SubjectType.ANIME,
                SubjectType.MUSIC,
                SubjectType.GAME,
                SubjectType.REAL
            )

            val results = coroutineScope {
                types.map { t ->
                    async {

                        val cateCode = when (t) {
                            SubjectType.BOOK -> SubjectBookCategory.COMIC.code
                            SubjectType.ANIME -> SubjectAnimeCategory.TV.code
                            SubjectType.GAME -> SubjectGameCategory.GAME.code
                            SubjectType.REAL -> SubjectRealCategory.JP_DRAMA.code
                            else -> 0
                        }


                        val result = subjectRepository.getSubjects(
                            type = t.code,
                            cat = cateCode,
                            limit = Constants.PAGE_SIZE,
                            offset = Constants.PAGE_START,
                            year = 2026,
                            sort = SortType.RANK.value
                        )
                        t to result
                    }
                }.awaitAll()
            }

            val newSections = mutableListOf<SubjectSection>()
            var errorMsg: String? = null

            for ((type, result) in results) {
                when (result) {
                    is NetworkResult.Success -> {
                        val items = result.data.data ?: emptyList()
                        newSections.add(SubjectSection(type = type, items = items))
                    }

                    is NetworkResult.HttpError -> {
                        if (errorMsg == null) errorMsg = "Subjects ${type.name} http ${result.code}"
                    }

                    is NetworkResult.NetworkError -> {
                        if (errorMsg == null) errorMsg = "Network error loading ${type.name}"
                    }

                    is NetworkResult.UnknownError -> {
                        if (errorMsg == null) errorMsg = "Unknown error loading ${type.name}"
                    }
                }
            }

            _uiState.update {
                it.copy(
                    isLoadingSubjects = false,
                    subjectSections = newSections,
                    error = errorMsg ?: it.error
                )
            }
        }
    }
}
