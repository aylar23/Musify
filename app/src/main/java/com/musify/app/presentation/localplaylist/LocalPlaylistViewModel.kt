package com.musify.app.presentation.localplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadRequest
import com.google.common.base.Preconditions
import com.musify.app.PlayerController
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.SongWithPlaylists
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocalPlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<PlaylistWithSongs>())

    val uiState = _uiState


    fun getPlayerController() = playerController

    fun getDownloadTracker() = downloadTracker

    fun getPlaylist(id: Long): Flow<PlaylistWithSongs> {
        return songRepository.getPlaylistWithSongs(id)


    }


    fun getAllPlaylists(): Flow<List<Playlist>> {

        return songRepository.getAllPlaylists(Playlist.PLAYLIST)

    }


    fun addNewPlaylist(name: String) {

        CoroutineScope(Dispatchers.IO).launch {
            songRepository.insertPlaylist(Playlist(name = name))

        }
    }


    fun addSongToPlaylist(song: Song, playlist: Playlist) {

        CoroutineScope(Dispatchers.IO).launch {
            songRepository.insertSong(song)

            val crossRef = PlaylistSongCrossRef(playlist.playlistId, song.songId)

            songRepository.insertPlaylistSongCrossRef(crossRef)

        }
        if (playlist.downloadable) {
            downloadTracker.download(song.toMediaItem())
        }
    }


    fun updateDownloadStatus(playlist: PlaylistWithSongs, downloadable: Boolean) {

        CoroutineScope(Dispatchers.IO).launch {
            songRepository.updateDownloadStatus(playlist.playlist.playlistId, downloadable)
        }

        if (downloadable) {
            playlist.songs.forEach { song ->
                downloadTracker.download(song.toMediaItem())
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                playlist.songs.forEach { song ->

                    val songPlaylists =
                        songRepository.getSongWithDownloadablePlaylists(song.songId).playlists
                    val downloadablePlaylistCount = songPlaylists.map { it.downloadable }.size

                    if (songPlaylists.size == 1 || downloadablePlaylistCount == 1) {
                        val uri =
                            Preconditions.checkNotNull(song.toMediaItem().localConfiguration).uri
                        val request = downloadTracker.getDownloadRequest(uri)
                        request?.let { it1 ->
                            downloadTracker.deleteDownloadRequest(it1)
                        }
                    }
                }

            }
        }

    }

    fun deleteSongFromPlaylist(playlist: Playlist, song: Song) {


        CoroutineScope(Dispatchers.IO).launch {


            val songPlaylists =
                songRepository.getSongWithDownloadablePlaylists(song.songId).playlists
            val downloadablePlaylistCount = songPlaylists.map { it.downloadable }.size

            if (songPlaylists.size == 1 || downloadablePlaylistCount == 1) {
                val uri = Preconditions.checkNotNull(song.toMediaItem().localConfiguration).uri
                val request = downloadTracker.getDownloadRequest(uri)
                request?.let { it1 ->
                    downloadTracker.deleteDownloadRequest(it1)
                }
            }

            songRepository.deleteSongFromPlaylist(playlist.playlistId, song.songId)


        }


    }


}
