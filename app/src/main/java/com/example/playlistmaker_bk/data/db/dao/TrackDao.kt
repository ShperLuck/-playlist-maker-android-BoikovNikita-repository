package com.example.playlistmaker_bk.data.db.dao

import androidx.room.*
import com.example.playlistmaker_bk.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks_table WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks_table WHERE isFavorite = 1")
    suspend fun getFavoriteTrackIds(): List<Long>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks_table WHERE trackId = :id AND isFavorite = 1)")
    suspend fun isFavorite(id: Long): Boolean
}