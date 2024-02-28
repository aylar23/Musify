package com.musify.app.domain.models

import android.net.Uri
import android.util.Log
import androidx.annotation.Keep
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.musify.app.di.DataModule.Companion.BASE_URL
import com.musify.app.player.PlayerStates
import java.lang.Exception
import javax.annotation.concurrent.Immutable

@Entity
@Immutable
data class Song(
    @SerializedName("id")
    @PrimaryKey val songId: Long ,
    val name: String,
    val artists: List<Artist> = emptyList(),
    val image: String = "",
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val duration: Long = 0L,
    @SerializedName("album_id")
    val albumId: Long? = null,
    @SerializedName("album_name")
    val albumName: String? = "",
    @SerializedName("album_year")
    val albumYear: Long? = 1999,
    val year: Long = 1999,
    val audio: String = "",
    var isSelected: Boolean = false,
    var isDownloaded: Boolean = false,
    var state: PlayerStates? = PlayerStates.STATE_IDLE,
) {

    fun getArtistsName(): String {
        val names = artists.map { it.name }.toMutableList()
        return names.joinToString(separator = ", ")
    }

    fun getArtist(): Artist {

        return artists[0]
    }

    fun getSongImage(): String {
        return image
    }

    fun getSongUrl(): String {
        var hslUrl = audio
        try{
            hslUrl = audio.removeSuffix(".mp3") + ".m3u8"
        }catch (e:Exception){
            Log.e("TAG", "getSongUrl: "+e.message )
        }

        return hslUrl
    }


    fun isPlaying(): Boolean {
        return state == PlayerStates.STATE_READY
                || state == PlayerStates.STATE_BUFFERING
                || state == PlayerStates.STATE_PLAYING


    }

    fun toMediaItem(): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setArtworkUri(Uri.parse(getSongImage()))
            .setTitle(name)
            .setDescription(getArtistsName())
            .setAlbumArtist(getArtistsName())
            .build()

        val trackUri = Uri.parse(getSongUrl())
        return MediaItem.Builder()
            .setUri(trackUri)
            .setMediaId(songId.toString())
            .setMediaMetadata(mediaMetaData)
            .build()
    }
}

