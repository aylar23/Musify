package com.musify.app.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.musify.app.PlayerController
import com.musify.app.domain.models.User
import com.musify.app.domain.models.defaultUser
import com.musify.app.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    var currentUser by mutableStateOf<User>(defaultUser)


}