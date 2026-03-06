package com.example.playlistmaker_bk.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onPlaylistsClick: () -> Unit, // Добавил для Чек-листа
    onFavoritesClick: () -> Unit, // Добавил для Чек-листа
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Playlist Maker",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // Кнопка перехода на экран Songs (Поиск)
            Button(
                onClick = onSearchClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Songs", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка перехода на экран Playlists
            Button(
                onClick = onPlaylistsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Playlists", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка перехода на экран Favorites
            Button(
                onClick = onFavoritesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Favorites", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка перехода на экран Настроек
            Button(
                onClick = onSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Settings", fontSize = 18.sp)
            }
        }
    }
}