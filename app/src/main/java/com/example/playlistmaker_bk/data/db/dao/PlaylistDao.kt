package com.example.playlistmaker_bk.data.db.dao

import androidx.room.*
import com.example.playlistmaker_bk.data.db.entity.PlaylistEntity
import com.example.playlistmaker_bk.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker_bk.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table ORDER BY id DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_table WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(crossRef: PlaylistTrackCrossRef)

    @Query("""
        SELECT * FROM favorite_tracks_table 
        INNER JOIN playlist_track_cross_ref ON favorite_tracks_table.trackId = playlist_track_cross_ref.trackId 
        WHERE playlist_track_cross_ref.playlistId = :playlistId
    """)
    fun getTracksForPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    // Вариант 2: Удаление связи трека и плейлиста
    @Delete
    suspend fun removeTrackFromPlaylist(crossRef: PlaylistTrackCrossRef)

    // Вариант 1: Удаление самого плейлиста
    @Query("DELETE FROM playlists_table WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)

    // Вариант 1 и 3: Удаление всех связей плейлиста
    @Query("DELETE FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun removeAllTracksFromPlaylist(playlistId: Long)

    // Вариант 3: Получение ID треков для склейки
    @Query("SELECT trackId FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun getTrackIdsForPlaylist(playlistId: Long): List<Long>
}