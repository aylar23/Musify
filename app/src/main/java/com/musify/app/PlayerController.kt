package com.musify.app

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.musify.app.domain.models.Song

import com.musify.app.player.MyPlayer
import com.musify.app.player.PlaybackState
import com.musify.app.player.PlayerEvents
import com.musify.app.player.PlayerStates
import com.musify.app.ui.utils.collectPlayerState
import com.musify.app.ui.utils.launchPlaybackStateJob
import com.musify.app.ui.utils.resetTracks
import com.musify.app.ui.utils.toMediaItemList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@OptIn(UnstableApi::class)
class PlayerController @Inject constructor(
    private val myPlayer: MyPlayer,
) : PlayerEvents {
    /**
     * A mutable state list of all tracks.
     */
    private val _tracks = mutableStateListOf<Song>()
    /**
     * An immutable snapshot of the current list of tracks.
     */
    val tracks: List<Song> get() = _tracks

    /**
     * A private Boolean variable to keep track of whether a track is currently being played or not.
     */
    private var isTrackPlay: Boolean = false

    /**
     * A public property backed by mutable state that holds the currently selected [Song].
     * It can only be set within the [HomeViewModel] class.
     */
    var selectedTrack: Song? by mutableStateOf(null)
        private set
    /**
     * A private property backed by mutable state that holds the index of the currently selected track.
     */
     var selectedTrackIndex: Int by mutableStateOf(-1)

    /**
     * A nullable [Job] instance that represents the ongoing process of updating the playback state.
     */
    private var playbackStateJob: Job? = null

    /**
     * A private [MutableStateFlow] that holds the current [PlaybackState].
     * It is used to emit updates about the playback state to observers.
     */
    private val _playbackState = MutableStateFlow(PlaybackState(0L, 0L))
    /**
     * A public property that exposes the [_playbackState] as an immutable [StateFlow] for observers.
     */
    val playbackState: StateFlow<PlaybackState> get() = _playbackState

    /**
     * A private Boolean variable to keep track of whether the track selection is automatic (i.e., due to the completion of a track) or manual.
     */
    private var isAuto: Boolean = false

    /**
     * Initializes the ViewModel. It populates the list of tracks, sets up the media player,
     * and observes the player state.
     */

    fun init(track: Song, songs: List<Song>){
        if (!_tracks.isEmpty()){
            _tracks.removeRange(0, tracks.size)
        }
        _tracks.addAll(songs)
        myPlayer.iniPlayer(songs.toMediaItemList())
        observePlayerState()
        onTrackClick(song = track)
    }


    fun onReorder(from:Int, to:Int){

        val song = tracks[from]

        _tracks.remove(song)
        _tracks.add(to, song)

    }


    /**
     * Handles track selection.
     *
     * @param index The index of the selected track in the track list.
     */
    private fun onTrackSelected(index: Int) {
        if (selectedTrackIndex == -1) isTrackPlay = true
        Log.e("TAG", "onTrackSelected: ", )
        if (selectedTrackIndex == -1 || selectedTrackIndex != index) {
            _tracks.resetTracks()
            selectedTrackIndex = index
            selectedTrack = tracks[selectedTrackIndex]
            setUpTrack()
        }
    }

    private fun setUpTrack() {
        if (!isAuto) myPlayer.setUpTrack(selectedTrackIndex, isTrackPlay)
        isAuto = false
    }

    /**
     * Updates the playback state and launches or cancels the playback state job accordingly.
     *
     * @param state The new player state.
     */
    private fun updateState(state: PlayerStates) {
        if (selectedTrackIndex != -1) {
            isTrackPlay = state == PlayerStates.STATE_PLAYING
            _tracks[selectedTrackIndex].state = state
            _tracks[selectedTrackIndex].isSelected = true
            selectedTrack = null
            selectedTrack = tracks[selectedTrackIndex]

            if (state == PlayerStates.STATE_NEXT_TRACK) {
                Log.e("TAG", "updateState: 1111", )
                isAuto = true
                onNextClick()
                myPlayer.emitPlaying()

            }
            updatePlaybackState(state)

            if (state == PlayerStates.STATE_END) onTrackSelected(0)
        }
    }


    private fun observePlayerState() {
        CoroutineScope(Dispatchers.Main).collectPlayerState(myPlayer, ::updateState)
    }

    private fun updatePlaybackState(state: PlayerStates) {
        playbackStateJob?.cancel()
        playbackStateJob =  CoroutineScope(Dispatchers.Main).launchPlaybackStateJob(_playbackState, state, myPlayer)
    }

    /**
     * Implementation of [PlayerEvents.onPreviousClick].
     * Changes to the previous track if one exists.
     */
    override fun onPreviousClick() {
        if (selectedTrackIndex > 0) onTrackSelected(selectedTrackIndex - 1)
    }

    /**
     * Implementation of [PlayerEvents.onNextClick].
     * Changes to the next track in the list if one exists.
     */
    override fun onNextClick() {
        if (selectedTrackIndex < tracks.size - 1) {
            Log.e("TAG", "onNextClick: ", )
            onTrackSelected(selectedTrackIndex + 1)
        }
    }

    /**
     * Implementation of [PlayerEvents.onPlayPauseClick].
     * Toggles play/pause state of the current track.
     */
    override fun onPlayPauseClick() {
        myPlayer.playPause()
    }

    /**
     * Implementation of [PlayerEvents.onTrackClick].
     * Selects the clicked track from the track list.
     *
     * @param song The track that was clicked.
     */
    override fun onTrackClick(song: Song) {
        onTrackSelected(tracks.indexOf(song))
    }

    override fun onTrackClick(song: Int) {

        onTrackSelected(song)
    }

    /**
     * Implementation of [PlayerEvents.onSeekBarPositionChanged].
     * Seeks to the specified position in the current track.
     *
     * @param position The position to seek to.
     */
    override fun onSeekBarPositionChanged(position: Long) {
        myPlayer.seekToPosition(position)
    }


//    fun getItem(id: String): MediaItem? {
//        return tracks.toMediaItemList().findLast { it.mediaId == id }
//    }
//
//    fun getRootItem(): MediaItem {
//        return MediaItemTree.treeNodes[MediaItemTree.ROOT_ID]!!.item
//    }
//
//    fun getChildren(id: String): List<MediaItem>? {
//        return MediaItemTree.treeNodes[id]?.getChildren()
//    }

//    fun getRandomItem(): MediaItem {
//        var curRoot = getRootItem()
//        while (curRoot.mediaMetadata.isBrowsable == true) {
//            val children = getChildren(curRoot.mediaId)!!
//            curRoot = children.random()
//        }
//        return curRoot
//    }

//    fun getItemFromTitle(title: String): MediaItem? {
//        return MediaItemTree.titleMap[title]?.item
//    }


}