package com.musify.app.domain.models

data class Album(
    val id: String,
    val name: String,
    val artist: String,
    val tracks: Int? = 0,
    val year: String? = ""
)
