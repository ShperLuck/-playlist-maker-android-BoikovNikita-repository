package com.example.playlistmaker_bk.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker_bk.R
import com.example.playlistmaker_bk.domain.models.Track
import com.example.playlistmaker_bk.ui.search.view_model.SearchState
import com.example.playlistmaker_bk.ui.search.view_model.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.showHistory()
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Поиск") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (it.isEmpty()) viewModel.showHistory() else viewModel.searchDebounce(it)
                },
                label = { Text("Введите название трека") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            viewModel.showHistory()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Очистить")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val currentState = state) {
                is SearchState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is SearchState.Content -> TrackList(currentState.tracks) {
                    viewModel.addTrackToHistory(it)
                    onTrackClick(it)
                }
                is SearchState.Empty -> Placeholder(R.drawable.ic_placeholder, "Ничего не найдено")
                is SearchState.Error -> Placeholder(
                    R.drawable.ic_placeholder,
                    "Ошибка сервера",
                    showRetry = true,
                    onRetry = { viewModel.doSearch(query) }
                )
                is SearchState.History -> if (currentState.tracks.isNotEmpty() && query.isEmpty()) {
                    Column {
                        Text("Вы искали", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
                        TrackList(currentState.tracks, onTrackClick)
                        Button(
                            onClick = { viewModel.clearHistory() },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Очистить историю")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Placeholder(imageRes: Int, textRes: String, showRetry: Boolean = false, onRetry: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(imageRes), contentDescription = null, modifier = Modifier.size(120.dp))
        Text(text = textRes, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
        if (showRetry) {
            Button(onClick = onRetry) { Text("Обновить") }
        }
    }
}

@Composable
fun TrackList(tracks: List<Track>, onTrackClick: (Track) -> Unit) {
    LazyColumn {
        items(tracks) { track ->
            TrackListItem(
                track = track,
                modifier = Modifier.clickable { onTrackClick(track) }
            )
        }
    }
}