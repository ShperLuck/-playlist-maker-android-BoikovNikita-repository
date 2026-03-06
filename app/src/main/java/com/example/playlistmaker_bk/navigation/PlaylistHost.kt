package com.example.playlistmaker_bk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.playlistmaker_bk.domain.models.Track
import com.example.playlistmaker_bk.ui.main.MainScreen
import com.example.playlistmaker_bk.ui.search.SearchScreen
import com.example.playlistmaker_bk.ui.search.view_model.SearchViewModel
import com.example.playlistmaker_bk.ui.details.TrackDetailsScreen
import com.example.playlistmaker_bk.ui.details.view_model.TrackDetailsViewModel
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker_bk.ui.playlists.PlaylistsListScreen
import com.example.playlistmaker_bk.ui.playlists.CreatePlaylistScreen
import com.example.playlistmaker_bk.ui.playlists.PlaylistDetailsScreen
import com.example.playlistmaker_bk.ui.favorites.FavoritesScreen
import com.example.playlistmaker_bk.ui.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker_bk.ui.settings.SettingsScreen
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.MAIN.route) {

        // Главный экран
        composable(Screens.MAIN.route) {
            MainScreen(
                onSearchClick = { navController.navigate(Screens.SEARCH.route) },
                onPlaylistsClick = { navController.navigate(Screens.PLAYLISTS.route) },
                onFavoritesClick = { navController.navigate(Screens.FAVORITES.route) },
                onSettingsClick = { navController.navigate(Screens.SETTINGS.route) }
            )
        }

        // Экран поиска
        composable(Screens.SEARCH.route) {
            val searchVm: SearchViewModel = koinViewModel()
            SearchScreen(
                viewModel = searchVm,
                onTrackClick = { track ->
                    val trackJson = Gson().toJson(track)
                    val encodedJson = URLEncoder.encode(trackJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("track_details/$encodedJson")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Экран деталей трека
        composable(
            route = "track_details/{trackJson}",
            arguments = listOf(navArgument("trackJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackJson = backStackEntry.arguments?.getString("trackJson") ?: ""
            val track = Gson().fromJson(trackJson, Track::class.java)

            val detailsVm: TrackDetailsViewModel = koinViewModel { parametersOf(track.trackId) }
            detailsVm.setTrack(track)

            TrackDetailsScreen(
                viewModel = detailsVm,
                onBack = { navController.popBackStack() },
                onNavigateToCreatePlaylist = {
                    navController.navigate(Screens.CREATE_PLAYLIST.route)
                } // Теперь кнопка в BottomSheet заработает
            )
        }

        // Список всех плейлистов
        composable(Screens.PLAYLISTS.route) {
            val playlistsVm: PlaylistsViewModel = koinViewModel()
            PlaylistsListScreen(
                viewModel = playlistsVm,
                addNewPlaylist = { navController.navigate(Screens.CREATE_PLAYLIST.route) },
                navigateBack = { navController.popBackStack() },
                navigateToPlaylist = { playlistId ->
                    navController.navigate("playlist_details/$playlistId")
                }
            )
        }

        // Детали конкретного плейлиста
        composable(
            route = "playlist_details/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            val playlistVm: PlaylistViewModel = koinViewModel { parametersOf(playlistId) }

            PlaylistDetailsScreen(
                viewModel = playlistVm,
                onBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    val trackJson = Gson().toJson(track)
                    val encodedJson = URLEncoder.encode(trackJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("track_details/$encodedJson")
                }
            )
        }

        // Создание нового плейлиста
        composable(Screens.CREATE_PLAYLIST.route) {
            val playlistsVm: PlaylistsViewModel = koinViewModel()
            CreatePlaylistScreen(
                viewModel = playlistsVm,
                onBack = { navController.popBackStack() }
            )
        }

        // Экран избранного
        composable(Screens.FAVORITES.route) {
            val favoritesVm: FavoritesViewModel = koinViewModel()
            FavoritesScreen(
                viewModel = favoritesVm,
                onBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    val trackJson = Gson().toJson(track)
                    val encodedJson = URLEncoder.encode(trackJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("track_details/$encodedJson")
                }
            )
        }

        // Настройки
        composable(Screens.SETTINGS.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}