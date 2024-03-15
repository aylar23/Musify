package com.musify.app.domain.paging

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.musify.app.domain.models.Song
import com.musify.app.domain.service.ApiService
import retrofit2.HttpException
import java.io.IOException

class SongPagingSource(
    private val artistId: Long,
    private val isTop: Int,
    private val isSingle: Int,
    private val apiService: ApiService
) : PagingSource<Int, Song>() {

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    @SuppressLint("LogNotTimber")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        try {
            val nextPageNumber = params.key ?: 1

            val response = apiService.getSongs(
                page = nextPageNumber, artistId = artistId, isTop = isTop, isSingle = isSingle
            )

            Log.e("TAG", "load: ", )
            return LoadResult.Page(
                data = response.results, prevKey = null, nextKey = response.next
            )

        } catch (e: IOException) {
            Log.e("PlaylistsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("PlaylistsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}