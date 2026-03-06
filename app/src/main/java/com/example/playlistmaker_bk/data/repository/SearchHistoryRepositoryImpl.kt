package com.example.playlistmaker_bk.data.repository

import com.example.playlistmaker_bk.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker_bk.domain.api.SearchHistoryRepository
import com.example.playlistmaker_bk.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val historyPreferences: SearchHistoryPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    override fun addTrack(track: Track) {
        val json = gson.toJson(track)
        historyPreferences.addEntry(json)
    }

    override suspend fun getHistory(): List<Track> {
        val entries = historyPreferences.getEntries()
        // Обработка ошибок десериализации на вякий случай, чтобы не упасть при битых данных
        return entries.mapNotNull {
            try {
                gson.fromJson(it, Track::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun clearHistory() {
        // ВЫЗЫВАТЬ ОЧИСТКУ!
        historyPreferences.clear()
    }
}