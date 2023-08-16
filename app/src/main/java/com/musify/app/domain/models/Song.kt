package com.musify.app.domain.models

data class Song(
    val id: Long,
    val title: String,
    val artist: Artist,
    val image: String,
    val liked: String,
    val albumId: String,
    val duration: Long
)
