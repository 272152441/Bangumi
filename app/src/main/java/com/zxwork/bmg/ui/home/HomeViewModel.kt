package com.zxwork.bmg.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.usecase.GetHomeCalendarsUseCase
import com.zxwork.bmg.domain.usecase.GetHomeSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeCalendarsUseCase: GetHomeCalendarsUseCase,
    private val getHomeSubjectsUseCase: GetHomeSubjectsUseCase
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
                val calendars = getHomeCalendarsUseCase()
                _uiState.update { it.copy(isLoading = false, calendars = calendars) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSubjects = true) }
            when (val result = getHomeSubjectsUseCase()) {
                is NetworkResult.Success -> {
                    val sections = result.data.map { 
                        SubjectSection(type = it.type, items = it.items)
                    }
                    _uiState.update {
                        it.copy(
                            isLoadingSubjects = false,
                            subjectSections = sections
                        )
                    }
                }
                is NetworkResult.HttpError -> {
                    _uiState.update { it.copy(isLoadingSubjects = false, error = result.message) }
                }
                is NetworkResult.NetworkError -> {
                    _uiState.update { it.copy(isLoadingSubjects = false, error = "Network Error") }
                }
                is NetworkResult.UnknownError -> {
                    _uiState.update { it.copy(isLoadingSubjects = false, error = "Unknown Error") }
                }
            }
        }
    }
}
