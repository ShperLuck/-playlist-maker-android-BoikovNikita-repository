package com.example.playlistmaker_bk.ui.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker_bk.domain.models.Track
import com.example.playlistmaker_bk.ui.favorites.view_model.FavoritesViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onTrackClick: (Track) -> Unit,
    onBack: () -> Unit
) {
    val favoriteTracks by viewModel.favoriteTracks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteTracks.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Ваш список избранного пуст")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(favoriteTracks) { track ->
                    ListItem(
                        headlineContent = { Text(track.trackName) },
                        supportingContent = { Text(track.artistName) },
                        modifier = Modifier.combinedClickable(
                            onClick = { onTrackClick(track) },
                            onLongClick = { viewModel.removeFromFavorites(track) }
                        )
                    )
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}