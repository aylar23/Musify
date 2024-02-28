package com.musify.app.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)

data class SongWithPlaylists(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "songId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val playlists: List<Playlist>
)