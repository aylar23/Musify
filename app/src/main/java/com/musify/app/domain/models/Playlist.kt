package com.musify.app.domain.models

data class Playlist(
    var id: Long,
    var image: String,
    var name: String,
    var songsCount: Int = 0
)
val defaultPlaylist = Playlist(1L, "image",  "Playlist", 8 )