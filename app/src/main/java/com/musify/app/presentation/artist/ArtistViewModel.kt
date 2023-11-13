package com.musify.app.presentation.artist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.Artist
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController
) : ViewModel() {


    private val _uiState = MutableStateFlow(BaseUIState<Artist>())

    val uiState = _uiState

    fun getPlayerController() = playerController


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


}