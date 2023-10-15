package com.musify.app.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import java.lang.reflect.Type

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun stringToArtists(data: String?): List<Artist> {
        val gson = Gson()
        if (data == null) {
            return emptyList()
        }
        val listType: Type = object : TypeToken<List<Artist>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun artistsToString(artists: List<Artist>): String {
        val gson = Gson()
        return gson.toJson(artists)
    }


    @TypeConverter
    fun stringToSongs(data: String?): List<Song> {
        val gson = Gson()
        if (data == null) {
            return emptyList()
        }
        val listType: Type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun songsToString(songs: List<Song>): String {
        val gson = Gson()
        return gson.toJson(songs)
    }


    @TypeConverter
    fun playlistToString(playlist: Playlist): String {
        val gson = Gson()
        return gson.toJson(playlist)
    }

    @TypeConverter
    fun stringToPlaylist(data: String?): Playlist? {
        val gson = Gson()
        val listType: Type = object : TypeToken<Playlist?>() {}.type
        return gson.fromJson(data, listType)
    }
}

