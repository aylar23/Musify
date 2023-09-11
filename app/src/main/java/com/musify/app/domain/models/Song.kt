package com.musify.app.domain.models

data class Song(
    val id: Long,
    val title: String,
    val artist: Artist,
    val image: String,
    val likes: Int,
    val isLiked: Boolean,
    val albumId: Long,
    val duration: Long
)
val defaultSong = Song(1L, "title", defaultArtist, "", 3, false, 1, 10000L  )