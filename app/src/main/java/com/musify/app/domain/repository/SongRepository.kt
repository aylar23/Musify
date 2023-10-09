package com.musify.app.domain.repository

import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.models.Song
import com.musify.app.domain.service.ApiService
import com.musify.app.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getMainScreen(): MainScreenData {
        return apiService.getMainScreenData()
    }


    suspend fun search(searchStr: String): SearchData {
        return apiService.search(searchStr)
    }

    suspend fun getArtist(id: Long): Artist {
        return apiService.getArtist(id)
    }


    suspend fun getPlaylist(id: Long, type:String): Playlist {
        return apiService.getPlaylist(type, id)
    }


}
