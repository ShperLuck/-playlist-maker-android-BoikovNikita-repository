package com.example.playlistmaker_bk.ui.playlists.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker_bk.domain.api.PlaylistsRepository
import com.example.playlistmaker_bk.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            playlistsRepository.getAllPlaylists().collect { list ->
                _playlists.value = list
            }
        }
    }

    // ВОЗВРАЩАЕМ ЭТОТ МЕТОД:
    fun createPlaylist(name: String, description: String, imageUri: String?) {
        viewModelScope.launch {
            playlistsRepository.addNewPlaylist(name, description, imageUri)
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylist(id)
        }
    }

    fun mergePlaylists(sourceId: Long, targetId: Long) {
        viewModelScope.launch {
            playlistsRepository.mergePlaylists(sourceId, targetId)
        }
    }
}