package com.musify.app.presentation.localplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.musify.app.PlayerController
import com.musify.app.domain.models.PlaylistWithSongs
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LocalPlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<PlaylistWithSongs>())

    val uiState = _uiState


    fun getPlayerController() = playerController


    fun getPlaylist(id: Long) {
        CoroutineScope(Dispatchers.IO).launch() {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.getPlaylistWithSongs(id)
                Log.e("TAG", "getPlaylist: "+data)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                Log.e("TAG", "getPlaylist: " + e.message)
                _uiState.update { it.updateToFailure() }
            }
        }


    }




}
