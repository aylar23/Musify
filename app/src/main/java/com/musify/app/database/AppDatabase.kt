package com.musify.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.musify.app.domain.dao.SongDao
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.Song


@Database(entities = [Playlist::class, Song::class, PlaylistSongCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun songDao(): SongDao
}