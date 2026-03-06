package com.example.playlistmaker_bk.domain.api

import com.example.playlistmaker_bk.domain.models.Playlist
import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(id: Long): Playlist
    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?)
    suspend fun addTrackToPlaylist(track: Track, playlistId: Long)
    fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>>

    // Новые методы для финального задания
    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun mergePlaylists(sourceId: Long, targetId: Long)
}