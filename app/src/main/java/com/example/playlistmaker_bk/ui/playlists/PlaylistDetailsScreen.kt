package com.example.playlistmaker_bk.ui.playlists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker_bk.R
import com.example.playlistmaker_bk.domain.models.Track
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker_bk.ui.search.TrackListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistViewModel,
    onBack: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlistData by viewModel.playlist.collectAsState()
    var trackToDelete by remember { mutableStateOf<Track?>(null) }
    var showDeletePlaylistDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlistData?.name ?: "Плейлист") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeletePlaylistDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить плейлист")
                    }
                }
            )
        }
    ) { padding ->
        playlistData?.let { data ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                AsyncImage(
                    model = data.coverImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_placeholder),
                    placeholder = painterResource(R.drawable.ic_placeholder)
                )

                Text(
                    text = data.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                if (data.description.isNotBlank()) {
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (data.tracks.isEmpty()) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("В плейлисте пусто")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(data.tracks) { track ->
                            TrackListItem(
                                track = track,
                                modifier = Modifier.combinedClickable(
                                    onClick = { onTrackClick(track) },
                                    onLongClick = { trackToDelete = track }
                                )
                            )
                        }
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        // Диалог удаления трека (Вариант 2)
        trackToDelete?.let { track ->
            AlertDialog(
                onDismissRequest = { trackToDelete = null },
                title = { Text("Удалить трек") },
                text = { Text("Вы уверены, что хотите удалить трек «${track.trackName}» из плейлиста?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.removeTrack(track.trackId)
                        trackToDelete = null
                    }) { Text("Да") }
                },
                dismissButton = {
                    TextButton(onClick = { trackToDelete = null }) { Text("Нет") }
                }
            )
        }

        // Диалог удаления плейлиста (Вариант 1)
        if (showDeletePlaylistDialog) {
            AlertDialog(
                onDismissRequest = { showDeletePlaylistDialog = false },
                title = { Text("Удалить плейлист") },
                text = { Text("Хотите удалить плейлист «${playlistData?.name}»?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deletePlaylist()
                        showDeletePlaylistDialog = false
                        onBack()
                    }) { Text("Да") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeletePlaylistDialog = false }) { Text("Нет") }
                }
            )
        }
    }
}