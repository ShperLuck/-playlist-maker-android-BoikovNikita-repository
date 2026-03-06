package com.example.playlistmaker_bk.ui.playlists.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker_bk.domain.api.PlaylistsRepository
import com.example.playlistmaker_bk.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val playlistId: Long
) : ViewModel() {

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            val baseInfo = playlistsRepository.getPlaylistById(playlistId)
            playlistsRepository.getTracksForPlaylist(playlistId).collect { trackList ->
                _playlist.value = baseInfo.copy(tracks = trackList)
            }
        }
    }

    fun removeTrack(trackId: Long) {
        viewModelScope.launch {
            playlistsRepository.removeTrackFromPlaylist(trackId, playlistId)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistsRepository.deletePlaylist(playlistId)
        }
    }
}