package com.musify.app

import com.musify.app.domain.models.Song
import com.musify.app.ui.utils.collectPlayerState
import com.musify.app.ui.utils.launchPlaybackStateJob
import com.musify.app.ui.utils.toMediaItemList




import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.musify.app.domain.repository.SongRepository
import com.musify.app.player.MyPlayer
import com.musify.app.player.PlaybackState
import com.musify.app.player.PlayerEvents
import com.musify.app.player.PlayerStates
import com.musify.app.player.PlayerStates.STATE_END
import com.musify.app.player.PlayerStates.STATE_NEXT_TRACK
import com.musify.app.player.PlayerStates.STATE_PLAYING
import com.musify.app.ui.utils.resetTracks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerController: PlayerController
) : ViewModel() {





    fun getPlayerController() = playerController
}