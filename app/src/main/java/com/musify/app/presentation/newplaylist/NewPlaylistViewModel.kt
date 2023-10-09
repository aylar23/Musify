package com.musify.app.presentation.newplaylist

import androidx.lifecycle.ViewModel
import com.musify.app.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewPlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

}