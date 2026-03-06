package com.example.playlistmaker_bk.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history") + SupervisorJob())
) {
    private val historyKey = stringPreferencesKey("search_history")

    fun addEntry(word: String) {
        if (word.isBlank()) return
        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[historyKey].orEmpty()
                val history = if (historyString.isNotEmpty()) historyString.split(SEPARATOR).toMutableList() else mutableListOf()

                history.remove(word)
                history.add(0, word)

                val updatedString = history.take(MAX_ENTRIES).joinToString(SEPARATOR)
                preferences[historyKey] = updatedString
            }
        }
    }

    suspend fun getEntries(): List<String> {
        return dataStore.data.map { preferences ->
            val historyString = preferences[historyKey].orEmpty()
            if (historyString.isEmpty()) emptyList() else historyString.split(SEPARATOR)
        }.first()
    }

    // ОЧИСТКА
    fun clear() {
        coroutineScope.launch {
            dataStore.edit { preferences ->
                preferences.remove(historyKey)
            }
        }
    }

    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = "%%"
    }
}