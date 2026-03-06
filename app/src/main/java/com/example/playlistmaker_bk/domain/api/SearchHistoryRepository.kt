package com.example.playlistmaker_bk.domain.api

import com.example.playlistmaker_bk.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    suspend fun getHistory(): List<Track>
    fun clearHistory()
}