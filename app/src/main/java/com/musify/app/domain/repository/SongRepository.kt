package com.musify.app.domain.repository

import com.musify.app.domain.models.Song
import com.musify.app.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SongRepository {

    /**
     * To Query song from remote db.
     */
    suspend fun getSong(
        query: String
    ): Flow<Resource<Song>>



}