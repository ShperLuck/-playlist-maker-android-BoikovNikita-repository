package com.example.playlistmaker_bk.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker_bk.data.db.dao.PlaylistDao
import com.example.playlistmaker_bk.data.db.dao.TrackDao
import com.example.playlistmaker_bk.data.db.entity.PlaylistEntity
import com.example.playlistmaker_bk.data.db.entity.TrackEntity
import com.example.playlistmaker_bk.data.db.entity.PlaylistTrackCrossRef

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}