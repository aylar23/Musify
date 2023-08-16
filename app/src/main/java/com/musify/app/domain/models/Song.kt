package com.musify.app.domain.models

data class Song(
    val id: String,
    val title: String,
    val Artist: String,
    val imageUrl: String,
    val liked: String,
    val AlbumId: String,
    val duration: Long,
    val isCurrent: Boolean = false,
    val isSelected: Boolean  = false
)
