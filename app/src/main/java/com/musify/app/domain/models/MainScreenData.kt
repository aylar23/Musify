package com.musify.app.domain.models

import com.google.gson.annotations.SerializedName

data class MainScreenData(
    val tops: List<Playlist>,
    @SerializedName("artists_of_the_week")
    val artists: List<Artist>,
    val playlists: List<Playlist>,
    @SerializedName("hit_songs")
    val hitSongs: List<Song>,

    )

val mainScreenData = MainScreenData(
    listOf(defaultPlaylist),
    artists = listOf(defaultArtist, defaultArtist, defaultArtist, defaultArtist),
    playlists = listOf(defaultPlaylist),
    hitSongs = listOf(
        defaultSong
    )
)