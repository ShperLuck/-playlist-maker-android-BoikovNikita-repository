package com.example.playlistmaker_bk.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker_bk.R
import com.example.playlistmaker_bk.ui.details.view_model.TrackDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    viewModel: TrackDetailsViewModel,
    onBack: () -> Unit,
    onNavigateToCreatePlaylist: () -> Unit // Добавил колбэк для перехода к созданию плейлиста
) {
    val track by viewModel.track.collectAsState()
    val playlists by viewModel.playlists.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали трека") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (track == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val currentTrack = track!!
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = currentTrack.artworkUrl100.replace("100x100bb.jpg", "512x512bb.jpg"),
                    contentDescription = null,
                    modifier = Modifier.size(312.dp).padding(bottom = 24.dp)
                )

                Text(
                    text = currentTrack.trackName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = currentTrack.artistName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(currentTrack.trackTimeMillis)

                Text(text = "Длительность: $formattedTime", fontSize = 16.sp)

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { viewModel.toggleFavorite(currentTrack) }) {
                        Icon(
                            imageVector = if (currentTrack.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (currentTrack.isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Button(
                        onClick = { showBottomSheet = true },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Добавить в плейлист")
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                    Text(
                        text = "Выберите плейлист",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        // Кнопка для быстрого создания нового плейлиста
                        item {
                            Button(
                                onClick = {
                                    showBottomSheet = false
                                    onNavigateToCreatePlaylist()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(stringResource(R.string.new_playlist))
                            }
                        }

                        items(playlists) { playlist ->
                            ListItem(
                                headlineContent = { Text(playlist.name) },
                                modifier = Modifier.clickable {
                                    track?.let { viewModel.addToPlaylist(it, playlist.id) }
                                    showBottomSheet = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}