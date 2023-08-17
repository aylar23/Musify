package com.musify.app.domain.models

data class Playlist(
    var id: String,
    var image: String,
    var name: String,
    var songsCount: Int? = 0
)
