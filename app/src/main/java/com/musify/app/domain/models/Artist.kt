package com.musify.app.domain.models

import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

@Immutable
data class Artist(
    val id: Long,
    val name: String,
    val image: String,
    @SerializedName("songs_count")
    val songsCount: Int = 0,
    val songs: List<Song> = emptyList(),
    val albums: List<Playlist> = emptyList(),
    val singles: List<Song> = emptyList(),
    @SerializedName("latest_release")
    val latestRelease: LatestRelease,

    ) {
    fun getArtistImage(): String {
        return image
    }
    fun hasLatestRelease(): Boolean {
        return latestRelease.album != null || latestRelease.song != null
    }
}


