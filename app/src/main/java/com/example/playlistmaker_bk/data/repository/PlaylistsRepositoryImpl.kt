package com.example.playlistmaker_bk.data.repository

import com.example.playlistmaker_bk.data.db.AppDatabase
import com.example.playlistmaker_bk.data.db.entity.PlaylistEntity
import com.example.playlistmaker_bk.data.db.entity.TrackEntity
import com.example.playlistmaker_bk.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker_bk.domain.api.PlaylistsRepository
import com.example.playlistmaker_bk.domain.models.Playlist
import com.example.playlistmaker_bk.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase
) : PlaylistsRepository {

    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                coverImageUri = coverImageUri
            )
        )
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(id = entity.id, name = entity.name, description = entity.description, coverImageUri = entity.coverImageUri)
            }
        }
    }

    override suspend fun getPlaylistById(id: Long): Playlist {
        val entity = playlistDao.getPlaylistById(id)
        return Playlist(id = entity.id, name = entity.name, description = entity.description, coverImageUri = entity.coverImageUri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val trackEntity = TrackEntity(
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
            isFavorite = track.isFavorite
        )
        trackDao.insertTrack(trackEntity)
        playlistDao.insertTrackToPlaylist(PlaylistTrackCrossRef(playlistId, track.trackId))
    }

    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>> {
        return playlistDao.getTracksForPlaylist(playlistId).map { entities ->
            entities.map { entity ->
                Track(entity.trackId, entity.trackName, entity.artistName, entity.trackTimeMillis, entity.artworkUrl100, entity.collectionName, entity.releaseDate, entity.primaryGenreName, entity.country, entity.previewUrl, entity.isFavorite)
            }
        }
    }

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) {
        playlistDao.removeTrackFromPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.removeAllTracksFromPlaylist(playlistId)
        playlistDao.deletePlaylistById(playlistId)
    }

    override suspend fun mergePlaylists(sourceId: Long, targetId: Long) {
        val trackIds = playlistDao.getTrackIdsForPlaylist(sourceId)
        trackIds.forEach { trackId ->
            playlistDao.insertTrackToPlaylist(PlaylistTrackCrossRef(targetId, trackId))
        }
        deletePlaylist(sourceId)
    }
}