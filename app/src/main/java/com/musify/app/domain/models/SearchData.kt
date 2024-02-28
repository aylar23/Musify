package com.musify.app.domain.models

import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable


@Immutable
data class SearchData(
    val tops: List<Playlist>,
    val artists: List<Artist>,
    val albums: List<Playlist>,
    val playlists: List<Playlist>,
    val songs: List<Song>,

    )
