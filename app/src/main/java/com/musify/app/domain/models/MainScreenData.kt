package com.musify.app.domain.models

import com.google.gson.annotations.SerializedName

data class MainScreenData(
    val tops: List<Playlist>,
    @SerializedName("artists_of_the_week")
    val artists: List<Artist>,
    val albums: List<Playlist>,
    @SerializedName("hit_songs")
    val songs: List<Song>,

)

