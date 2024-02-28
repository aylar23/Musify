package com.musify.app.domain.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Query
import androidx.room.Transaction
import com.musify.app.domain.dao.SongDao
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.SongWithPlaylists
import com.musify.app.domain.service.ApiService
import com.musify.app.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao,
) {


    suspend fun getMainScreen(): MainScreenData {
        return apiService.getMainScreenData()
    }


    suspend fun search(searchStr: String): SearchData {
        return apiService.search(searchStr)
    }

    suspend fun getArtist(id: Long): Artist {
        return apiService.getArtist(id)
    }


    suspend fun getPlaylist(id: Long, type:String): Playlist {
        return apiService.getPlaylist(type, id)
    }

     fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs> {
        return songDao.getPlaylistWithSongs(id)
    }

     fun getAllPlaylists(type: String): Flow<MutableList<Playlist>> {
        return songDao.getAllPlaylists(type)
    }

//    fun getAllPlaylists() = Pager(
//        PagingConfig(
//            DEFAULT_PAGE_SIZE,
//            prefetchDistance = 1
//        )
//    ){
//        PlaylistsPagingSource(
//           songDao = songDao
//        )
//    }.flow


    suspend fun insertPlaylist(playlist: Playlist) {
        return songDao.insertPlaylist(playlist)
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        return songDao.updatePlaylist(playlist.playlistId, playlist.name)
    }

    suspend fun deletePlaylist(playlist: Playlist) {
        return songDao.deletePlaylist(playlist)
    }

    suspend fun deleteSongFromPlaylist(playlistId:Long, songId: Long) {
        return songDao.deleteSongFromPlaylist(playlistId, songId)
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }


    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef) {
        return songDao.insertPlaylistSongCrossRef(playlistSongCrossRef)
    }


    suspend fun updateDownloadStatus(id:Long, downloadable: Boolean) {
        return songDao.updateDownloadStatus(id, downloadable)
    }

    suspend fun getPlaylistSongs(id: Long): PlaylistWithSongs {
        return songDao.getPlaylistSongs(id)

    }

    suspend fun getSongWithDownloadablePlaylists(id: Long): SongWithPlaylists{
        return songDao.getSongWithDownloadablePlaylists(id)

    }

    fun playlistExists(id: Long): Flow<Boolean> {
        return songDao.playlistExists(id)

    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

}
