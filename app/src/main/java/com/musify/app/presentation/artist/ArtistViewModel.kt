package com.musify.app.presentation.artist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<Artist>())

    val uiState = _uiState
    var id = savedStateHandle.getStateFlow("id", 0L)


    init {
        getArtistDetail(id.value)
    }
    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker


    fun getArtistDetail(id:Long) {

        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }

            try {
                    val data = songRepository.getArtist(id)
                    _uiState.update { it.updateToLoaded(data) }
                } catch (e: Exception) {
                    Log.e("TAG", "getMainPageData: " + e.message)
                    _uiState.update { it.updateToFailure() }
                }
            }


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

    fun setID(id: Long) {
        savedStateHandle["id"] = id

    }


}