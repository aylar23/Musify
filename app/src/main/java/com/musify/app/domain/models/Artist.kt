package com.musify.app.domain.models

data class Artist(
    val id: String,
    val name: String,
    val songsCount: Int = 0,
    val albumsCount: Int = 0
)
