package com.musify.app.domain.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PlaylistCategory(
    val id: Long = 0L,
    val name: String,
    val playlists : List<Playlist>
) {
}
