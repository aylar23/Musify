package com.musify.app.models

import com.musify.app.player.PlayerStates
import com.musify.app.player.PlayerStates.STATE_IDLE


data class Track(
    val trackId: Int = 0,
    val trackName: String = "",
    val trackUrl: String = "",
    val trackImage: Int = 0,
    val artistName: String = "",
    var state: PlayerStates = STATE_IDLE
)