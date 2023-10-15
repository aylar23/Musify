package com.musify.app.player

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.FragmentManager
import androidx.media3.common.C
import androidx.media3.common.DrmInitData
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.drm.DrmSession.DrmSessionException
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadHelper.LiveContentUnsupportedException
import androidx.media3.exoplayer.offline.DownloadIndex
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.google.common.base.Preconditions
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet


@OptIn(UnstableApi::class)
class DownloadTracker(
    context: Context,
    dataSourceFactory: DataSource.Factory,
    downloadManager: DownloadManager
)  {
    private val TAG = "DownloadTracker"
    var context: Context
    var dataSourceFactory :DataSource.Factory

    init {
        this.context = context
        this.dataSourceFactory = dataSourceFactory
    }
    private var listeners: CopyOnWriteArraySet<Listener> = CopyOnWriteArraySet()
    var downloads: HashMap<Uri, Download>? = HashMap()
    private var downloadIndex: DownloadIndex? = downloadManager.downloadIndex


    init {
//        downloadManager.addListener(DownloadManagerListener())
        loadDownloads()
    }


    /** Listens for changes in the tracked downloads.  */
    interface Listener {
        /** Called when the tracked downloads changed.  */
        fun onDownloadsChanged()
    }





//    private var startDownloadDialogHelper: StartDownloadDialogHelper? =
//        null


    fun addListener(listener: Listener) {
        listeners.add(Preconditions.checkNotNull(listener))
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun isDownloaded(mediaItem: MediaItem): Boolean {
        val download = downloads!![Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        return download != null && download.state != Download.STATE_FAILED
    }

    fun getDownloadRequest(uri: Uri): DownloadRequest? {
        val download = downloads!![uri]
        return if (download != null && download.state != Download.STATE_FAILED) download.request else null
    }

    fun toggleDownload(
        fragmentManager: FragmentManager?, mediaItem: MediaItem, renderersFactory: RenderersFactory?
    ) {
        val download = downloads!![Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        if (download != null && download.state != Download.STATE_FAILED) {
            DownloadService.sendRemoveDownload(
                context,
                DownloadService::class.java,
                download.request.id,  /* foreground= */
                false
            )
        }
    }

    private fun loadDownloads() {
        try {
            downloadIndex!!.getDownloads().use { loadedDownloads ->
                while (loadedDownloads.moveToNext()) {
                    val download = loadedDownloads.download
                    downloads!![download.request.uri] = download
                }
            }
        } catch (e: IOException) {
            Log.w(TAG, "Failed to query downloads", e)
        }
    }

//    private class DownloadManagerListener : DownloadManager.Listener {
//        override fun onDownloadChanged(
//            downloadManager: DownloadManager, download: Download, finalException: Exception?
//        ) {
//            downloads.put(download.request.uri, download)
//            for (listener in listeners) {
//                listener.onDownloadsChanged()
//            }
//        }
//
//        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
//            downloads.remove(download.request.uri)
//            for (listener in listeners) {
//                listener.onDownloadsChanged()
//            }
//        }
//    }

//    private class StartDownloadDialogHelper(
//        private val fragmentManager: FragmentManager,
//        private val downloadHelper: DownloadHelper,
//        private val mediaItem: MediaItem
//    ) : DownloadHelper.Callback, TrackSelectionDialog.TrackSelectionListener,
//        DialogInterface.OnDismissListener {
//        private var trackSelectionDialog: TrackSelectionDialog? = null
//        private var widevineOfflineLicenseFetchTask: androidx.media3.demo.main.DownloadTracker.WidevineOfflineLicenseFetchTask? =
//            null
//        private var keySetId: ByteArray?
//
//        init {
//            downloadHelper.prepare(this)
//        }
//
//        fun release() {
//            downloadHelper.release()
//            if (trackSelectionDialog != null) {
//                trackSelectionDialog.dismiss()
//            }
//            if (widevineOfflineLicenseFetchTask != null) {
//                widevineOfflineLicenseFetchTask.cancel(false)
//            }
//        }
//
//        // DownloadHelper.Callback implementation.
//        override fun onPrepared(helper: DownloadHelper) {
//            val format = getFirstFormatWithDrmInitData(helper)
//            if (format == null) {
//                onDownloadPrepared(helper)
//                return
//            }
//
//            // The content is DRM protected. We need to acquire an offline license.
//            if (Util.SDK_INT < 18) {
//                Toast.makeText(
//                    context,
//                    R.string.error_drm_unsupported_before_api_18,
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                Log.e(
//                    DownloadTracker.TAG,
//                    "Downloading DRM protected content is not supported on API versions below 18"
//                )
//                return
//            }
//            // TODO(internal b/163107948): Support cases where DrmInitData are not in the manifest.
//            if (!hasNonNullWidevineSchemaData(format.drmInitData)) {
//                Toast.makeText(
//                    context,
//                    R.string.download_start_error_offline_license,
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                Log.e(
//                    DownloadTracker.TAG,
//                    "Downloading content where DRM scheme data is not located in the manifest is not"
//                            + " supported"
//                )
//                return
//            }
//            widevineOfflineLicenseFetchTask =
//                androidx.media3.demo.main.DownloadTracker.WidevineOfflineLicenseFetchTask(
//                    format,
//                    mediaItem.localConfiguration!!.drmConfiguration,
//                    dataSourceFactory,  /* dialogHelper= */
//                    this,
//                    helper
//                )
//            widevineOfflineLicenseFetchTask.execute()
//        }
//
//        override fun onPrepareError(helper: DownloadHelper, e: IOException) {
//            val isLiveContent = e is LiveContentUnsupportedException
//            val toastStringId: Int =
//                if (isLiveContent) R.string.download_live_unsupported else R.string.download_start_error
//            val logMessage =
//                if (isLiveContent) "Downloading live content unsupported" else "Failed to start download"
//            Toast.makeText(context, toastStringId, Toast.LENGTH_LONG).show()
//            Log.e(DownloadTracker.TAG, logMessage, e)
//        }
//
//        // TrackSelectionListener implementation.
//        fun onTracksSelected(trackSelectionParameters: TrackSelectionParameters?) {
//            for (periodIndex in 0 until downloadHelper.periodCount) {
//                downloadHelper.clearTrackSelections(periodIndex)
//                downloadHelper.addTrackSelection(periodIndex, trackSelectionParameters!!)
//            }
//            val downloadRequest = buildDownloadRequest()
//            if (downloadRequest.streamKeys.isEmpty()) {
//                // All tracks were deselected in the dialog. Don't start the download.
//                return
//            }
//            startDownload(downloadRequest)
//        }
//
//        // DialogInterface.OnDismissListener implementation.
//        override fun onDismiss(dialogInterface: DialogInterface) {
//            trackSelectionDialog = null
//            downloadHelper.release()
//        }
//        // Internal methods.
//        /**
//         * Returns the first [Format] with a non-null [Format.drmInitData] found in the
//         * content's tracks, or null if none is found.
//         */
//        private fun getFirstFormatWithDrmInitData(helper: DownloadHelper): Format? {
//            for (periodIndex in 0 until helper.periodCount) {
//                val mappedTrackInfo = helper.getMappedTrackInfo(periodIndex)
//                for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
//                    val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
//                    for (trackGroupIndex in 0 until trackGroups.length) {
//                        val trackGroup = trackGroups[trackGroupIndex]
//                        for (formatIndex in 0 until trackGroup.length) {
//                            val format = trackGroup.getFormat(formatIndex)
//                            if (format.drmInitData != null) {
//                                return format
//                            }
//                        }
//                    }
//                }
//            }
//            return null
//        }
//
//        private fun onOfflineLicenseFetched(helper: DownloadHelper, keySetId: ByteArray) {
//            this.keySetId = keySetId
//            onDownloadPrepared(helper)
//        }
//
//        private fun onOfflineLicenseFetchedError(e: DrmSessionException) {
//            Toast.makeText(
//                context,
//                R.string.download_start_error_offline_license,
//                Toast.LENGTH_LONG
//            )
//                .show()
//            Log.e(DownloadTracker.TAG, "Failed to fetch offline DRM license", e)
//        }
//
//        private fun onDownloadPrepared(helper: DownloadHelper) {
//            if (helper.periodCount == 0) {
//                Log.d(DownloadTracker.TAG, "No periods found. Downloading entire stream.")
//                startDownload()
//                downloadHelper.release()
//                return
//            }
//            val tracks = downloadHelper.getTracks( /* periodIndex= */0)
//            if (!TrackSelectionDialog.willHaveContent(tracks)) {
//                Log.d(DownloadTracker.TAG, "No dialog content. Downloading entire stream.")
//                startDownload()
//                downloadHelper.release()
//                return
//            }
//            trackSelectionDialog = TrackSelectionDialog.createForTracksAndParameters( /* titleId= */
//                R.string.exo_download_description,
//                tracks,
//                DownloadHelper.getDefaultTrackSelectorParameters(context),  /* allowAdaptiveSelections= */
//                false,  /* allowMultipleOverrides= */
//                true,  /* onTracksSelectedListener= */
//                this,  /* onDismissListener= */
//                this
//            )
//            trackSelectionDialog.show(fragmentManager,  /* tag= */null)
//        }
//
//        /**
//         * Returns whether any [DrmInitData.SchemeData] that [ ][DrmInitData.SchemeData.matches] [C.WIDEVINE_UUID] has non-null [ ][DrmInitData.SchemeData.data].
//         */
//        private fun hasNonNullWidevineSchemaData(drmInitData: DrmInitData?): Boolean {
//            for (i in 0 until drmInitData!!.schemeDataCount) {
//                val schemeData = drmInitData[i]
//                if (schemeData.matches(C.WIDEVINE_UUID) && schemeData.hasData()) {
//                    return true
//                }
//            }
//            return false
//        }
//
//        private fun startDownload(downloadRequest: DownloadRequest = buildDownloadRequest()) {
//            DownloadService.sendAddDownload(
//                context, DemoDownloadService::class.java, downloadRequest,  /* foreground= */false
//            )
//        }
//
//        private fun buildDownloadRequest(): DownloadRequest {
//            return downloadHelper
//                .getDownloadRequest(
//                    Util.getUtf8Bytes(
//                        Preconditions.checkNotNull(
//                            mediaItem.mediaMetadata.title.toString()
//                        )
//                    )
//                )
//                .copyWithKeySetId(keySetId)
//        }
//    }
}