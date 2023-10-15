package com.musify.app.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val songs: List<Song>
)