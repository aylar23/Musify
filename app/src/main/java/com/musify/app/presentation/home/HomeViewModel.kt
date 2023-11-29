package com.musify.app.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.PlayerController
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.DownloadTracker
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


    fun getPlayerController() = playerController


    fun getMainPageData() {

        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update { it.updateToLoading() }

            try {
                val data = songRepository.getMainScreen()

                _uiState.update { it.updateToLoaded(data) }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure() }
            }


        }
    }


//    private fun buildDownloadRequest(): DownloadRequest {
//            return DownloadHelper()
//                .getDownloadRequest(
//                    Util.getUtf8Bytes(
//                        Preconditions.checkNotNull(
//                            mediaItem.mediaMetadata.title.toString()
//                        )
//                    )
//                )
//                .copyWithKeySetId(keySetId)
//        }
//

}