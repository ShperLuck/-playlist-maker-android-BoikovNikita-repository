package com.example.playlistmaker_bk.ui.details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker_bk.domain.api.TracksRepository
import com.example.playlistmaker_bk.domain.api.PlaylistsRepository
import com.example.playlistmaker_bk.domain.models.Track
import com.example.playlistmaker_bk.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TrackDetailsViewModel(
    private val trackId: Long,
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    init {
        loadPlaylists()
    }

    fun setTrack(track: Track) {
        _track.value = track
        checkFavoriteStatus()
    }

    private fun checkFavoriteStatus() {
        viewModelScope.launch {
            // Оптимизация: проверяем только один ID через репозиторий
            // Если в репозитории нет такого метода, используй:
            tracksRepository.getFavoriteTracks().collect { favorites ->
                val isFav = favorites.any { it.trackId == trackId }
                _track.value = _track.value?.copy(isFavorite = isFav)
            }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistsRepository.getAllPlaylists().collect { list ->
                _playlists.value = list
            }
        }
    }

    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            val newStatus = !track.isFavorite
            tracksRepository.toggleFavorite(track, newStatus)
            // Мгновенно обновляем UI
            _track.value = track.copy(isFavorite = newStatus)
        }
    }

    fun addToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch {
            playlistsRepository.addTrackToPlaylist(track, playlistId)
            loadPlaylists()
        }
    }
}