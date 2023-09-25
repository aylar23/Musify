package com.musify.app.domain.models

data class Song(
    val id: Long,
    val title: String,
    val artist: Artist,
    val image: String,
    val likes: Int,
    val isLiked: Boolean,
    val album: Album,
    val duration: Long
)
val defaultSong = Song(1L, "title", defaultArtist, "", 3, false, defaultAlbum, 10000L  )