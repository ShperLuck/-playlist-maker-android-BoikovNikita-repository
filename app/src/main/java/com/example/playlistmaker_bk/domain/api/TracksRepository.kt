package com.example.playlistmaker_bk.domain.api

import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun toggleFavorite(track: Track, isFavorite: Boolean)
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}