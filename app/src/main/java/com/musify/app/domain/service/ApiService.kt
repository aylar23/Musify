package com.musify.app.domain.service

import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.Paging
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.SearchData
import com.musify.app.domain.models.Song
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("api/main/")
    suspend fun getMainScreenData(): MainScreenData


    @GET("api/search/")
    suspend fun search(
        @Query("s") s: String,
        @Query("search_type") type: String
    ): SearchData

    @GET("api/artists/{id}/")
    suspend fun getArtist(
        @Path("id") id: Long
    ): Artist


    @GET("api/{type}/{id}/")
    suspend fun getPlaylist(
        @Path("type") type: String,
        @Path("id") id: Long
    ): Playlist


    @GET("/api/albums/")
    suspend fun getAlbums(
        @Query("page") page: Int,
        @Query("artist") artistId: Long
    ): Paging<Playlist>

    @GET("/api/songs/")
    suspend fun getSongs(
        @Query("page") page: Int,
        @Query("artist") artistId: Long,
        @Query("is_top") isTop: Int,
        @Query("is_single") isSingle: Int,
    ): Paging<Song>

}