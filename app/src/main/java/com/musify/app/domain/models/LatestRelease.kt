package com.musify.app.domain.models

data class LatestRelease(
    val song: Song?,
    val album: Playlist?,
) {

    fun getName(): String {
       return album?.name ?: song?.name ?: ""
    }

    fun getYear(): String {
        return (album?.year ?: song?.year ?: 2000).toString()
    }

    fun getImage(): String {
        return (album?.image ?: song?.image ?: "").toString()
    }
}
