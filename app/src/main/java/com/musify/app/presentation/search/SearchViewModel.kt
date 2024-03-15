package com.musify.app.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.di.DataStoreUtil
import com.musify.app.di.SearchDataStore
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Playlist.Companion.ALL_SEARCH
import com.musify.app.domain.models.Playlist.Companion.PLAYLIST
import com.musify.app.domain.models.PlaylistSongCrossRef
import com.musify.app.domain.models.SearchData
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
class SearchViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val searchDataStore: SearchDataStore = SearchDataStore.getInstance(dataStoreUtil.dataStore)



    private val _uiState = MutableStateFlow(BaseUIState<SearchData>())

    val uiState = _uiState

    val type = savedStateHandle.getStateFlow("type", ALL_SEARCH)

    var searchStr = savedStateHandle.getStateFlow("SEARCH", "")


    val keys = searchDataStore.searchFlow


    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    fun search(s: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.search(s, type.value)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure() }
            }
        }
    }


    fun saveSearchStr(s:String){

        if (s.isNotEmpty()){
            viewModelScope.launch {
                searchDataStore.saveNewMsg(s)
            }
        }

    }

    fun clearSearchStr(){
        viewModelScope.launch {
            searchDataStore.clearAllMyMessages()
        }
    }

    fun setSearch(s:String){
        savedStateHandle["SEARCH"] = s

    }

    fun setType(type:String){
        savedStateHandle["type"] = type
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return songRepository.getAllPlaylists(PLAYLIST)
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

