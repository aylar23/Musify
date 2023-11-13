package com.musify.app.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.Song
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {


    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): List<Playlist>


    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId= :id")
    fun getPlaylistWithSongs(id:Long): PlaylistWithSongs


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)


}