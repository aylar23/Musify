package com.musify.app.domain.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.musify.app.domain.dao.SongDao
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Playlist.Companion.PLAYLIST
import retrofit2.HttpException
import java.io.IOException


//class PlaylistsPagingSource (
//    private val songDao: SongDao,
//): PagingSource<Int, Playlist>() {
//
//    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
//        try{
//            val nextPageNumber = params.key ?: 1
//
//            val response = songDao.getAllPlaylists(PLAYLIST)
//
//            val data = response
//            Log.e("PlaylistsPagingSource", response.toString())
//            return LoadResult.Page(
//                data = data,
//                prevKey = null,
//                nextKey = null
//            )
//        }catch (e: IOException){
//            Log.e("PlaylistsPagingSource", e.message ?: "error")
//            return LoadResult.Error(e)
//        } catch (e: HttpException){
//            Log.e("PlaylistsPagingSource",  e.message ?: "error")
//            return LoadResult.Error(e)
//        }
//    }
//
//}