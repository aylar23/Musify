package com.musify.app.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.SongWithPlaylists
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {


    @Query("SELECT Playlist.*," +
            "( SELECT  COUNT(*) FROM  PlaylistSongCrossRef " +
            "WHERE   playlistId = playlist.playlistId) AS songsCount  " +
            "FROM  playlist WHERE type LIKE '%' || :type || '%'")
    fun getAllPlaylists(type:String): Flow<MutableList<Playlist>>


//    @Query("SELECT Playlist.*," +
//            "( SELECT  COUNT(*) FROM  PlaylistSongCrossRef " +
//            "WHERE   playlistId = playlist.playlistId) AS songsCount  " +
//            "FROM  playlist WHERE type LIKE :type")
//    fun getAllPagingPlaylists(type:String): PagingSource<Int, Playlist>


    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId= :id")
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: Playlist)


    @Query("UPDATE Playlist SET name = :name WHERE playlistId= :id")
    fun updatePlaylist(id:Long, name: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)



    @Query("UPDATE Playlist SET downloadable = :downloadable WHERE playlistId =:id")
    fun updateDownloadStatus(id:Long, downloadable: Boolean)


    @Delete
    fun deletePlaylist(playlist: Playlist)

    @Query("DELETE FROM PlaylistSongCrossRef WHERE playlistId= :playlistId AND songId = :songId")
    fun deleteSongFromPlaylist(playlistId:Long, songId: Long)

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId= :id")
    fun getPlaylistSongs(id: Long): PlaylistWithSongs

    @Transaction
    @Query("SELECT * FROM Song WHERE songId= :id")
    fun getSongWithDownloadablePlaylists(id: Long): SongWithPlaylists

    @Query("SELECT EXISTS(SELECT * FROM Playlist WHERE playlistId = :id)")
    fun playlistExists(id : Long) : Flow<Boolean>
}