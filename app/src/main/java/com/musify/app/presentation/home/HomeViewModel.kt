package com.musify.app.presentation.home

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.offline.Download
import com.musify.app.PlayerController
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.Song
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import com.musify.app.player.buildRenderersFactory
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<MainScreenData>())
    val uiState: StateFlow<BaseUIState<MainScreenData>> = _uiState.asStateFlow()



    init {
        getMainPageData()
    }



    fun getDownloadTracker() = downloadTracker


    fun getPlayerController() = playerController


    fun getMainPageData() {


        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update { it.updateToLoading() }

            try {
                val data = songRepository.getMainScreen()

                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                Log.e("TAG", "getMainPageData: "+e.message)
                _uiState.update { it.updateToFailure() }
            }


        }
    }


     fun toggleDownload(song: Song) {
        downloadTracker.download( song.toMediaItem())
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