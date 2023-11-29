package com.musify.app.presentation.search

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.di.DataStoreUtil
import com.musify.app.di.SearchDataStore
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.repository.SongRepository
import com.musify.app.ui.utils.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val searchDataStore: SearchDataStore = SearchDataStore.getInstance(dataStoreUtil.dataStore)



    private val _uiState = MutableStateFlow(BaseUIState<SearchData>())

    val uiState = _uiState

    var searchStr = savedStateHandle.getStateFlow<String>("SEARCH", "")


    val keys = searchDataStore.searchFlow


    fun getPlayerController() = playerController

    fun search(s: String) {
        viewModelScope.launch {
            _uiState.update { it.updateToLoading() }
            try {
                val data = songRepository.search(s)
                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                Log.e("TAG", "getMainPageData: " + e.message)
                _uiState.update { it.updateToFailure() }
            }
        }


    }


    fun saveSearchStr(s:String){
        viewModelScope.launch {
            searchDataStore.saveNewMsg(s)
        }
    }

    fun removeSearchStr(s:String){
        viewModelScope.launch {
            searchDataStore.removeMsg(s)
        }
    }
    fun setSearch(s:String){
        savedStateHandle["SEARCH"] = s

    }

}

