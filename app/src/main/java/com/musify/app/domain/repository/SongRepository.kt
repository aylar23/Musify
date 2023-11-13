package com.musify.app.domain.repository

import com.musify.app.domain.dao.SongDao
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.models.Song
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

    suspend fun getPlaylistWithSongs(id: Long): PlaylistWithSongs {
        return songDao.getPlaylistWithSongs(id)
    }

    suspend fun getAllPlaylists(): List<Playlist> {
        return songDao.getAllPlaylists()
    }


    suspend fun insertPlaylist(playlist: Playlist) {
        return songDao.insertPlaylist(playlist)
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }


    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef) {
        return songDao.insertPlaylistSongCrossRef(playlistSongCrossRef)
    }

}
