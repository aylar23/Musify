package com.musify.app.domain.models

data class Album(
    val id: String,
    val name: String,
    val artist: Artist,
    val image: String,
    val tracks: Int? = 0,
    val year: String? = ""
)
