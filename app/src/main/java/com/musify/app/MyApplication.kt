package com.musify.app

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.musify.app.player.PlaybackService
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        val factory = MediaController.Builder(
            applicationContext,
            SessionToken(applicationContext, ComponentName(applicationContext, PlaybackService::class.java))
        ).buildAsync()
    }
}