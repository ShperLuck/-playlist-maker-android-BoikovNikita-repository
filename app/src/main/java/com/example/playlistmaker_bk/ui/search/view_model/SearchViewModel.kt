package com.example.playlistmaker_bk.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker_bk.domain.api.Resource
import com.example.playlistmaker_bk.domain.api.SearchHistoryRepository
import com.example.playlistmaker_bk.domain.api.TracksRepository
import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchState {
    object Loading : SearchState
    data class Content(val tracks: List<Track>) : SearchState
    data class Error(val message: String) : SearchState
    object Empty : SearchState
    data class History(val tracks: List<Track>) : SearchState
}

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Content(emptyList()))
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(2000L)
            if (changedText.isNotEmpty()) {
                doSearch(changedText)
            } else {
                showHistory()
            }
        }
    }

    fun doSearch(text: String) {
        if (text.isEmpty()) return

        viewModelScope.launch {
            _state.value = SearchState.Loading
            tracksRepository.searchTracks(text).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isNullOrEmpty()) {
                            _state.value = SearchState.Empty
                        } else {
                            _state.value = SearchState.Content(resource.data)
                        }
                    }
                    is Resource.Error -> _state.value = SearchState.Error(resource.message ?: "")
                    is Resource.Loading -> _state.value = SearchState.Loading
                }
            }
        }
    }

    fun showHistory() {
        viewModelScope.launch {
            val history = historyRepository.getHistory()
            if (history.isNotEmpty()) {
                _state.value = SearchState.History(history)
            } else {
                _state.value = SearchState.Content(emptyList())
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        historyRepository.addTrack(track)
    }

    fun clearHistory() {
        historyRepository.clearHistory()
        _state.value = SearchState.Content(emptyList())
    }
}