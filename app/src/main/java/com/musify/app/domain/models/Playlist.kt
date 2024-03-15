package com.musify.app.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import com.musify.app.di.DataModule
import javax.annotation.concurrent.Immutable

@Entity
@Immutable
data class Playlist(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0L,
    val name: String,
    val artists: List<Artist> = emptyList(),
    val songs: List<Song>? = emptyList(),
    val image: String = "",
    @SerializedName("songs_count")
    val songsCount: Int = 0,
    val year: Int = 0,
    var type: String = PLAYLIST,
    val downloadable: Boolean = false
){
    fun getPlaylistImage(): String {
        return image
    }

    fun getArtistsName(): String {
        val names = artists.map {it.name }.toMutableList()
        return names.joinToString( separator = ", ")
    }

    companion object{
        const val ALL = ""
        const val PLAYLIST = "playlist"
        const val ALBUM = "album"
        const val ALL_SEARCH = "all"
        const val ARTIST = "artist"
        const val SONG = "song"
    }
}

