package com.musify.app




import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerController: PlayerController
) : ViewModel() {





    fun getPlayerController() = playerController
}