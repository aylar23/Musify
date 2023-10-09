package com.musify.app.di

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.extractor.DefaultExtractorsFactory
import com.musify.app.player.MyPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @OptIn(UnstableApi::class) @Provides
    @Singleton
    fun provideExoPLayer(context: Context): ExoPlayer {


        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val httpDataSourceFactory = OkHttpDataSource.Factory(OkHttpClient.Builder().build())

// Create a HLS media source pointing to a playlist uri.
        val hlsMediaSource =
            HlsMediaSource.Factory(httpDataSourceFactory)
//
//
//            .setMediaSourceFactory()
// Create a player instance.

        val player = ExoPlayer.Builder(context).setMediaSourceFactory(hlsMediaSource).build()
        return player
    }


    @Provides
    @Singleton
    fun provideMyPlayer(player: ExoPlayer): MyPlayer {
        return MyPlayer(player)
    }


}