package com.musify.app.presentation.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.musify.app.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val artistId = savedStateHandle.getStateFlow("artistId", 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val albums = artistId
        .flatMapLatest { artistId ->
            songRepository.getAlbums(artistId)
        }
        .cachedIn(viewModelScope)



    fun setArtistId(artistId: Long) {
        savedStateHandle["artistId"] = artistId
    }
}

