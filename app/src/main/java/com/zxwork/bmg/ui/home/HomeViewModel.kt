package com.zxwork.bmg.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxwork.bmg.data.repository.CalendarRepository
import com.zxwork.bmg.data.repository.SubjectRepository
import com.zxwork.bmg.data.model.SubjectType
import com.zxwork.bmg.data.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                val calendars = calendarRepository.getCalendars()
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
            val sections = mutableListOf<SubjectSection>()
            for (t in types) {
                when (val res = subjectRepository.getSubjects(type = t.code, cat = 0, limit = 10, offset = 0)) {
                    is NetworkResult.Success -> {
                        val items = res.data.data ?: emptyList()
                        sections.add(SubjectSection(type = t, items = items))
                    }
                    is NetworkResult.HttpError -> {
                        _uiState.update { it.copy(error = "Subjects ${t.name} http ${res.code}") }
                    }
                    is NetworkResult.NetworkError -> {
                        _uiState.update { it.copy(error = "Network error loading ${t.name}") }
                    }
                    is NetworkResult.UnknownError -> {
                        _uiState.update { it.copy(error = "Unknown error loading ${t.name}") }
                    }
                }
            }
            _uiState.update { it.copy(isLoadingSubjects = false, subjectSections = sections) }
        }
    }
}
