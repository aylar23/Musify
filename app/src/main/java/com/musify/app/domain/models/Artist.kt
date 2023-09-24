package com.musify.app.domain.models

data class Artist(
    val id: Long,
    val name: String,
    val image: String,
    val songsCount: Int = 0,
    val songs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList(),
    val singles: List<Song> = emptyList(),

    )


val defaultArtist = Artist(1L, "Artist", "image", 5)