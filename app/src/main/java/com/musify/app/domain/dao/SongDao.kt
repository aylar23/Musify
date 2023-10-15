package com.musify.app.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.Song
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {


    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>


    @Transaction
    @Query("SELECT * FROM Playlist WHERE id= :id")
    fun getUsersAndLibraries(id:Long): List<PlaylistWithSongs>


    @Insert
    fun insertSong(song: Song)

    @Insert
    fun insertPlaylist(playlist: Playlist)




}