package com.example.playlistmaker_bk.ui.favorites.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker_bk.domain.api.TracksRepository
import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    val favoriteTracks: StateFlow<List<Track>> = tracksRepository
        .getFavoriteTracks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFromFavorites(track: Track) {
        viewModelScope.launch {
            tracksRepository.toggleFavorite(track, false)
        }
    }
}