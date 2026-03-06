package com.example.playlistmaker_bk.di

import androidx.room.Room
import com.example.playlistmaker_bk.data.db.AppDatabase
import com.example.playlistmaker_bk.data.network.ITunesApi
import com.example.playlistmaker_bk.data.network.RetrofitNetworkClient
import com.example.playlistmaker_bk.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker_bk.data.preferences.dataStore
import com.example.playlistmaker_bk.data.repository.PlaylistsRepositoryImpl
import com.example.playlistmaker_bk.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker_bk.data.repository.TracksRepositoryImpl
import com.example.playlistmaker_bk.domain.api.PlaylistsRepository
import com.example.playlistmaker_bk.domain.api.SearchHistoryRepository
import com.example.playlistmaker_bk.domain.api.TracksRepository
import com.example.playlistmaker_bk.ui.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker_bk.ui.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker_bk.ui.search.view_model.SearchViewModel
import com.example.playlistmaker_bk.ui.details.view_model.TrackDetailsViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration() // Удалять старые данные
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single { RetrofitNetworkClient(get()) }
    single { SearchHistoryPreferences(androidContext().dataStore) }

    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }

    factory { Gson() }
}

val viewModelModule = module {
    viewModel { SearchViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }

    viewModel { (trackId: Long) -> TrackDetailsViewModel(trackId, get(), get()) }
    viewModel { (playlistId: Long) -> PlaylistViewModel(get(), playlistId) }
}