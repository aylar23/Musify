package com.musify.app.presentation.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<Playlist>())

    val uiState = _uiState


    fun getPlayerController() = playerController



    fun getPlaylist(id:Long, type: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.getPlaylist(id, type)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure() }
            }
        }


    }

}
