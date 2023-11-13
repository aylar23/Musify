package com.musify.app.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import com.musify.app.di.DataModule

@Entity
data class Playlist(
    @SerializedName("id")
    @PrimaryKey val playlistId: Long,
    val name: String,
    val artists: List<Artist>,
    val songs: List<Song>?,
    val image: String,
    val year: Int = 0
){
    fun getPlaylistImage(): String {
        return image
    }

    fun getArtistsName(): String {
        val names = artists.map {it.name }.toMutableList()
        return names.joinToString( separator = ", ")
    }
}

