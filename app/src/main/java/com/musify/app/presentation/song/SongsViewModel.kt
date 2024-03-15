package com.musify.app.presentation.song

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.musify.app.PlayerController
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.Song
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SongsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
) : ViewModel() {

    val artistId = savedStateHandle.getStateFlow("artistId", 0L)
    val isTop = savedStateHandle.getStateFlow("isTop", 0)
    val isSingle = savedStateHandle.getStateFlow("isSingle", 0)

    fun getDownloadTracker() = downloadTracker

    fun getPlayerController() = playerController


    @OptIn(ExperimentalCoroutinesApi::class)
    val songs = combine(artistId, isTop, isSingle) { artistId, isTop, isSingle ->
        Triple(artistId, isTop, isSingle)
    }.flatMapLatest {
        songRepository.getSongs(it.first, it.second, it.third)
    }.cachedIn(viewModelScope)


    fun setArtistId(artistId: Long) {
        savedStateHandle["artistId"] = artistId
    }

    fun setIsTop(isSingle: Int) {
        savedStateHandle["isSingle"] = isSingle
    }

    fun setIsSingle(isTop: Int) {
        savedStateHandle["isTop"] = isTop
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return songRepository.getAllPlaylists(Playlist.PLAYLIST)
    }

    fun addNewPlaylist(name:String){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.insertPlaylist(Playlist(name = name))

        }
    }


    fun addSongToPlaylist(song: Song, playlist: Playlist){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.insertSong(song)

            val crossRef = PlaylistSongCrossRef(playlist.playlistId, song.songId)

            songRepository.insertPlaylistSongCrossRef(crossRef)

        }
        if (playlist.downloadable){
            downloadTracker.download(song.toMediaItem())
        }
    }


}
