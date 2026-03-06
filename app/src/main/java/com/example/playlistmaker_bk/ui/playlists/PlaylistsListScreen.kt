package com.example.playlistmaker_bk.ui.playlists

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable // ДОБАВЛЕНО
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.playlistmaker_bk.R
import com.example.playlistmaker_bk.domain.models.Playlist
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistsListScreen(
    viewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateBack: () -> Unit,
    navigateToPlaylist: (Long) -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()
    val context = LocalContext.current

    var selectedPlaylistForAction by remember { mutableStateOf<Playlist?>(null) }
    var showActionDialog by remember { mutableStateOf(false) }
    var showMergeSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Плейлисты") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = addNewPlaylist) {
                Text("Новый плейлист")
            }
        }
    ) { padding ->
        if (playlists.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("У вас пока нет плейлистов")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(playlists) { playlist ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .combinedClickable(
                                onClick = { navigateToPlaylist(playlist.id) },
                                onLongClick = {
                                    selectedPlaylistForAction = playlist
                                    showActionDialog = true
                                }
                            )
                    ) {
                        Column {
                            AsyncImage(
                                model = playlist.coverImageUri,
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.ic_placeholder),
                                error = painterResource(R.drawable.ic_placeholder),
                                modifier = Modifier.aspectRatio(1f),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = playlist.name,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

        if (showActionDialog) {
            AlertDialog(
                onDismissRequest = { showActionDialog = false },
                title = { Text(selectedPlaylistForAction?.name ?: "") },
                text = { Text("Выберите действие для плейлиста") },
                confirmButton = {
                    TextButton(onClick = {
                        showMergeSheet = true
                        showActionDialog = false
                    }) { Text("Склеить с...") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        selectedPlaylistForAction?.let { viewModel.deletePlaylist(it.id) }
                        showActionDialog = false
                    }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        if (showMergeSheet) {
            ModalBottomSheet(onDismissRequest = { showMergeSheet = false }) {
                Text(
                    "Выберите плейлист для слияния",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn(Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                    items(playlists.filter { it.id != selectedPlaylistForAction?.id }) { target ->
                        ListItem(
                            headlineContent = { Text(target.name) },
                            modifier = Modifier.clickable { // Теперь ошибка исчезнет
                                selectedPlaylistForAction?.let { source ->
                                    viewModel.mergePlaylists(source.id, target.id)
                                    Toast.makeText(context, "${source.name} слит с ${target.name}", Toast.LENGTH_SHORT).show()
                                }
                                showMergeSheet = false
                            }
                        )
                    }
                }
            }
        }
    }
}