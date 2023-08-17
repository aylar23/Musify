package com.musify.app.domain.models

data class Album(
    val id: Long,
    val name: String,
    val artist: Artist,
    val image: String,
    val tracks: Int = 0,
    val year: String = ""
)
