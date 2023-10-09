package com.musify.app.domain.models

import android.util.Log
import com.musify.app.di.DataModule.Companion.BASE_URL
import com.musify.app.player.PlayerStates

data class Song(
    val id: Long,
    val name: String,
    val artists: List<Artist>,
    val image: String,
    val likes: Int,
    val isLiked: Boolean,
    val album: Playlist,
    val duration: Long,
    val audio: String,
    var isSelected: Boolean = false,
    var state: PlayerStates = PlayerStates.STATE_IDLE
) {

    fun getArtistsName(): String {
        val names = artists.map {it.name }.toMutableList()
        return names.joinToString( separator = ", ")
    }

    fun getArtist(): Artist {

        return artists[0]
    }

    fun getSongImage(): String {
        return BASE_URL+image
    }

    fun getSongUrl(): String {
        return "$BASE_URL$audio.m3u8"
    }
}

