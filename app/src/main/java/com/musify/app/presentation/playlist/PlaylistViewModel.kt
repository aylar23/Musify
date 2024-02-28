package com.musify.app.presentation.playlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Playlist.Companion.ALBUM
import com.musify.app.domain.models.Playlist.Companion.PLAYLIST
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.Song
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<Playlist>())

    val uiState = _uiState

    fun getDownloadTracker() = downloadTracker

    fun getPlayerController() = playerController

    var type = savedStateHandle.getStateFlow<String>("type", "playlist")

    var id = savedStateHandle.getStateFlow<Long>("id", 0L)






    init {
        getPlaylist(id.value, type.value)
    }

    fun getPlaylist(id: Long, type: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.getPlaylist(id, type)
                Log.e("TAG", "getPlaylist: "+data)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                Log.e("TAG", "getPlaylist: " + e.message)
                _uiState.update { it.updateToFailure() }
            }
        }


    }
    fun setPlaylistIdAndType(id: Long, type: String) {

        savedStateHandle["type"] = type
        savedStateHandle["id"] = id
    }
    fun savePlaylist(playlist: Playlist) {

        playlist.type = if (type.value == ALBUM) ALBUM else PLAYLIST
        CoroutineScope(Dispatchers.IO).launch() {

            songRepository.insertPlaylist(playlist = playlist)
            if (playlist.songs == null) return@launch

            for (song in playlist.songs) {

                songRepository.insertSong(song)

                val crossRef = PlaylistSongCrossRef(playlist.playlistId, song.songId)
                songRepository.insertPlaylistSongCrossRef(crossRef)
            }
        }
    }


    fun getAllPlaylists(): Flow<List<Playlist>> {
        return songRepository.getAllPlaylists(PLAYLIST)
    }


    fun playlistExists(): Flow<Boolean> {
        return songRepository.playlistExists(id.value)
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


    fun toggleDownload(song: Song) {
        downloadTracker.download( song.toMediaItem())
    }
}
