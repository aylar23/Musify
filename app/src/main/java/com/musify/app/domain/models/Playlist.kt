package com.musify.app.domain.models

data class Playlist(
    var id: String,
    var name: String,
    var songsCount: Int? = 0
)
