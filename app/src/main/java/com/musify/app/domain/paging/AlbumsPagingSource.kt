package com.musify.app.domain.paging

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.repository.SongRepository
import com.musify.app.domain.service.ApiService
import retrofit2.HttpException
import java.io.IOException


class AlbumsPagingSource (
    private val artistId: Long,
    private val apiService: ApiService
): PagingSource<Int, Playlist>() {

    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    @SuppressLint("LogNotTimber")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
        try{
            val nextPageNumber = params.key ?: 1

            val response = apiService.getAlbums(page = nextPageNumber,artistId = artistId)

            return LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = response.next
            )

        }catch (e: IOException){
            Log.e("PlaylistsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException){
            Log.e("PlaylistsPagingSource",  e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}