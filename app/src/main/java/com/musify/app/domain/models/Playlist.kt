package com.musify.app.domain.models

data class Playlist(
    var id: Long,
    var name: String,
    var songsCount: Int = 0
)
