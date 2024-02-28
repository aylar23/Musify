package com.musify.app.player

import com.musify.app.domain.models.Song


/**
 * An interface for handling player events such as play, pause, next, previous, and seek bar position changes.
 */
interface PlayerEvents {

    /**
     * Invoked when the play or pause button is clicked.
     */
    fun onPlayPauseClick()

    /**
     * Invoked when the previous button is clicked.
     */
    fun onPreviousClick()

    /**
     * Invoked when the next button is clicked.
     */
    fun onNextClick()

    /**
     * Invoked when a track is clicked. The clicked [Song] is provided as a parameter.
     *
     * @param song The track that was clicked.
     */
    fun onTrackClick(song: Song)

    fun onPlayNext(song: Song)

    fun onTrackClick(song: Int)
    /**
     * Invoked when the position of the seek bar has changed. The new position is provided as a parameter.
     *
     * @param position The new position of the seek bar.
     */
    fun onSeekBarPositionChanged(position: Long)
}
