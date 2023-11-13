package com.musify.app.domain.models

import android.util.Log
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.musify.app.di.DataModule.Companion.BASE_URL
import com.musify.app.player.PlayerStates

@Entity
data class Song(
    @SerializedName("id")
    @PrimaryKey val songId: Long,
    val name: String,
    val artists: List<Artist>,
    val image: String,
    val likes: Int,
    val isLiked: Boolean,
    val album: Playlist?,
    val duration: Long,
    val audio: String,
    var isSelected: Boolean = false,
    var state: PlayerStates? = PlayerStates.STATE_IDLE,
) {

    fun getArtistsName(): String {
        val names = artists.map { it.name }.toMutableList()
        return names.joinToString(separator = ", ")
    }

    fun getArtist(): Artist {

        return artists[0]
    }

    fun getSongImage(): String {
        return image
    }

    fun getSongUrl(): String {
        return "$audio.m3u8"
    }


    fun isPlaying(): Boolean {
        return state == PlayerStates.STATE_READY
                || state == PlayerStates.STATE_BUFFERING
                || state == PlayerStates.STATE_PLAYING


    }
}

