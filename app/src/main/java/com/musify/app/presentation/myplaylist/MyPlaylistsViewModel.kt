package com.musify.app.presentation.myplaylist

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.common.base.Preconditions
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class MyPlaylistsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<List<Playlist>>())
    val uiState: StateFlow<BaseUIState<List<Playlist>>> = _uiState.asStateFlow()

    val type = savedStateHandle.getStateFlow("type", "")


    fun getAllPlaylists(): Flow<MutableList<Playlist>> {

       return songRepository.getAllPlaylists("")

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val playlists = type
        .flatMapLatest { type ->
            songRepository.getAllPlaylists(type)
        }

    fun addNewPlaylist(name:String){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.insertPlaylist(Playlist(name = name))

        }
    }

    fun updatePlaylist(playlist: Playlist){

        CoroutineScope(Dispatchers.IO).launch{
            songRepository.updatePlaylist(playlist)

        }
    }


    fun setType(type:String){
        savedStateHandle["type"] = type
    }
    fun deletePlaylist(playlist: Playlist){


        CoroutineScope(Dispatchers.IO).launch{

            val songs = songRepository.getPlaylistSongs(playlist.playlistId).songs

            songs.forEach{song ->

                val songPlaylists = songRepository.getSongWithDownloadablePlaylists(song.songId).playlists
                val downloadablePlaylistCount = songPlaylists.map { it.downloadable }.size

                if (songPlaylists.size ==1 || downloadablePlaylistCount==1){
                    val uri = Preconditions.checkNotNull(song.toMediaItem().localConfiguration).uri
                    val request = downloadTracker.getDownloadRequest(uri)
                    request?.let { it1 ->
                        downloadTracker.deleteDownloadRequest(it1)
                    }
                }

            }
            songRepository.deletePlaylist(playlist)




        }



    }
}