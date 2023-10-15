package com.musify.app.presentation.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController
) : ViewModel() {

    private val _uiState = MutableStateFlow(BaseUIState<SearchData>())

    val uiState = _uiState

    var searchStr = savedStateHandle.getStateFlow<String>("SEARCH", "")


    fun getPlayerController() = playerController

    fun search(s: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.search(s)
                Log.e("TAG", "search: " + data)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                Log.e("TAG", "getMainPageData: " + e.message)
                _uiState.update { it.updateToFailure() }
            }
        }


    }


    fun setSearch(s:String){
        savedStateHandle["SEARCH"] = s

    }

}

