
package com.musify.app.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.DefaultRenderersFactory.ExtensionRendererMode
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import java.io.File
import java.util.concurrent.Executors

val USE_CRONET_FOR_NETWORKING = true


val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"

fun useExtensionRenderers(): Boolean {
    return true
}



@Synchronized
fun getDownloadDirectory(context: Context): File? {

    var downloadDirectory = context.getExternalFilesDir( /* type= */null)

    if (downloadDirectory == null) {
        downloadDirectory = context.filesDir
    }

    return downloadDirectory
}

@UnstableApi
fun buildRenderersFactory(
    context: Context, preferExtensionRenderer: Boolean
): RenderersFactory {
    val extensionRendererMode: @ExtensionRendererMode Int =
        if (useExtensionRenderers()) (
                if (preferExtensionRenderer)
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
    return DefaultRenderersFactory(context).setExtensionRendererMode(extensionRendererMode)
}








