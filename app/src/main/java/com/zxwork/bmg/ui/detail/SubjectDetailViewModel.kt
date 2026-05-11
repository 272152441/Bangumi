package com.zxwork.bmg.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxwork.bmg.core.NetworkResult
import com.zxwork.bmg.domain.usecase.GetCharactersUseCase
import com.zxwork.bmg.domain.usecase.GetEpisodesUseCase
import com.zxwork.bmg.domain.usecase.GetLinkSubjectsUseCase
import com.zxwork.bmg.domain.usecase.GetSubjectDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectDetailViewModel @Inject constructor(
    private val getSubjectDetailUseCase: GetSubjectDetailUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getLinkSubjectsUseCase: GetLinkSubjectsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubjectDetailState())
    val uiState: StateFlow<SubjectDetailState> = _uiState.asStateFlow()

    private val subjectId: Int? = savedStateHandle["subjectId"]

    init {
        subjectId?.let { loadData(it) }
    }

    fun onIntent(intent: SubjectDetailIntent) {
        when (intent) {
            is SubjectDetailIntent.LoadDetail -> loadData(intent.id)
            SubjectDetailIntent.Refresh -> subjectId?.let { loadData(it) }
        }
    }

    private fun loadData(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            coroutineScope {
                val detailDeferred = async { getSubjectDetailUseCase(id) }
                val episodesDeferred = async { getEpisodesUseCase(id) }
                val charactersDeferred = async { getCharactersUseCase(id) }
                val linkSubjectDeferred = async { getLinkSubjectsUseCase(id) }

                val detailResult = detailDeferred.await()
                val episodesResult = episodesDeferred.await()
                val charactersResult = charactersDeferred.await()
                val linkSubjectResult = linkSubjectDeferred.await()

                when (detailResult) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                subject = detailResult.data,
                                episodes = if (episodesResult is NetworkResult.Success) episodesResult.data else emptyList(),
                                characters = if (charactersResult is NetworkResult.Success) charactersResult.data else emptyList(),
                                linkSubjects =
                                    if (linkSubjectResult is NetworkResult.Success) linkSubjectResult.data else emptyList()
                            )
                        }
                    }

                    is NetworkResult.HttpError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "HTTP ${detailResult.code}: ${detailResult.message}"
                            )
                        }
                    }

                    is NetworkResult.NetworkError -> {
                        _uiState.update { it.copy(isLoading = false, error = "Network Error") }
                    }

                    is NetworkResult.UnknownError -> {
                        _uiState.update { it.copy(isLoading = false, error = "Unknown Error") }
                    }
                }
            }
        }
    }
}
