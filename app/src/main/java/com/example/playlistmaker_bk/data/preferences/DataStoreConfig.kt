package com.example.playlistmaker_bk.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Создаем один экземпляр DataStore на все приложение
// "settings"
val Context.dataStore by preferencesDataStore(name = "settings")