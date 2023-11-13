package com.musify.app.presentation.myplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MyPlaylistsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<List<Playlist>>())
    val uiState: StateFlow<BaseUIState<List<Playlist>>> = _uiState.asStateFlow()

    init {
        getAllPlaylists()
    }

    fun getAllPlaylists() {


        CoroutineScope(Dispatchers.IO).launch() {
            _uiState.update { it.updateToLoading() }

            try {
                val data = songRepository.getAllPlaylists()
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure() }
            }
        }


    }
}