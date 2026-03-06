package com.example.playlistmaker_bk.data.repository

import com.example.playlistmaker_bk.data.db.AppDatabase
import com.example.playlistmaker_bk.data.db.entity.TrackEntity
import com.example.playlistmaker_bk.data.network.ITunesApi
import com.example.playlistmaker_bk.domain.api.Resource
import com.example.playlistmaker_bk.domain.api.TracksRepository
import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val itunesApi: ITunesApi,
    private val database: AppDatabase
) : TracksRepository {

    private val trackDao = database.trackDao()

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        emit(Resource.Loading())
        try {
            val response = itunesApi.search(expression)
            val favoriteIds = trackDao.getFavoriteTrackIds().toSet()

            val tracks = response.results.map { dto ->
                Track(
                    trackId = dto.trackId.toLong(),
                    trackName = dto.trackName ?: "",
                    artistName = dto.artistName ?: "",
                    trackTimeMillis = dto.trackTimeMillis ?: 0L,
                    artworkUrl100 = dto.artworkUrl100 ?: "",
                    collectionName = dto.collectionName,
                    releaseDate = dto.releaseDate,
                    primaryGenreName = dto.primaryGenreName,
                    country = dto.country,
                    previewUrl = dto.previewUrl,
                    isFavorite = favoriteIds.contains(dto.trackId.toLong())
                )
            }
            emit(Resource.Success(tracks))
        } catch (e: Exception) {
            emit(Resource.Error("Ошибка сети"))
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks().map { entities ->
            entities.map { entity ->
                Track(
                    trackId = entity.trackId,
                    trackName = entity.trackName,
                    artistName = entity.artistName,
                    trackTimeMillis = entity.trackTimeMillis,
                    artworkUrl100 = entity.artworkUrl100,
                    collectionName = entity.collectionName,
                    releaseDate = entity.releaseDate,
                    primaryGenreName = entity.primaryGenreName,
                    country = entity.country,
                    previewUrl = entity.previewUrl,
                    isFavorite = entity.isFavorite
                )
            }
        }
    }

    override suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        //  Обновлять её, заменяя флаг isFavorite.
        //  Остать в таблице (для плейлистов), но исчезнуть из "Избранного".
        trackDao.insertTrack(
            TrackEntity(
                trackId = track.trackId,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl,
                createdAt = System.currentTimeMillis(),
                isFavorite = isFavorite // Сохранять переданный статус
            )
        )
    }
}