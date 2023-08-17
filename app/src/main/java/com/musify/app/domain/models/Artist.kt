package com.musify.app.domain.models

data class Artist(
    val id: Long,
    val name: String,
    val image: String,
    val songsCount: Int = 0
)
