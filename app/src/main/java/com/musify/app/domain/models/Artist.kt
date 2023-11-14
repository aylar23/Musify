package com.musify.app.domain.models

import com.google.gson.annotations.SerializedName
import com.musify.app.di.DataModule

data class Artist(
    val id: Long,
    val name: String,
    val image: String,
    @SerializedName("songs_count")
    val songsCount: Int = 0,
    val songs: List<Song> = emptyList(),
    val albums: List<Playlist> = emptyList(),
    val singles: List<Song> = emptyList(),

){
    fun getArtistImage(): String {
        return image
    }
}


val defaultArtist = Artist(1L, "Artist", "image", 5)