package com.musify.app.domain.models

import com.musify.app.di.DataModule

data class Playlist(
    val id: Long,
    val name: String,
    val artists: List<Artist>,
    val songs: List<Song>,
    val image: String,
    val year: Int = 0
){
    fun getPlaylistImage(): String {
        return DataModule.BASE_URL +image
    }

    fun getArtistsName(): String {
        val names = artists.map {it.name }.toMutableList()
        return names.joinToString( separator = ", ")
    }
}

