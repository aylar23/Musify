package com.musify.app.domain.service

import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.MainScreenData
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.SearchData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("api/main/")
    suspend fun getMainScreenData(): MainScreenData


    @GET("api/search/")
    suspend fun search(
        @Query("s") s:String
    ): SearchData

    @GET("api/artists/{id}/")
    suspend fun getArtist(
        @Path("id") id:Long
    ): Artist



    @GET("api/{type}/{id}/")
    suspend fun getPlaylist(
        @Path("type") type:String,
        @Path("id") id:Long
    ): Playlist



}