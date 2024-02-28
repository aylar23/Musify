package com.musify.app.player

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.AsyncTask
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateMapOf
import androidx.media3.common.C
import androidx.media3.common.C.TrackType
import androidx.media3.common.DrmInitData
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.DrmConfiguration
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.drm.DrmSession.DrmSessionException
import androidx.media3.exoplayer.drm.DrmSessionEventListener
import androidx.media3.exoplayer.drm.OfflineLicenseHelper
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadHelper.LiveContentUnsupportedException
import androidx.media3.exoplayer.offline.DownloadIndex
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.ui.TrackSelectionView
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import com.musify.app.R
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet


@OptIn(UnstableApi::class)
class DownloadTracker(
    private val context: Context,
    private val dataSourceFactory: DataSource.Factory,
    private val downloadManager: DownloadManager
) {
    val TAG = "DownloadTracker"


    private var listeners: CopyOnWriteArraySet<Listener> = CopyOnWriteArraySet()
    var downloads = mutableStateMapOf<Uri, Download>()
//    val downloads: Flow<HashMap<Uri, Download>> = _downloads.va

    private var downloadIndex: DownloadIndex? = downloadManager.downloadIndex

    init {
        downloadManager.addListener(DownloadManagerListener())
        loadDownloads()
    }


    /** Listens for changes in the tracked downloads.  */
    interface Listener {
        /** Called when the tracked downloads changed.  */
        fun onDownloadsChanged()
    }


    var startDownloadDialogHelper: StartDownloadDialogHelper? = null


    fun addListener(listener: Listener) {
        listeners.add(Preconditions.checkNotNull(listener))
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun isDownloaded(mediaItem: MediaItem): Boolean {
        val download = downloads[Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        return download != null && download.state == Download.STATE_COMPLETED
    }

    fun isDownloading(mediaItem: MediaItem): Boolean {
        val download = downloads[Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        return download != null && download.state == Download.STATE_DOWNLOADING
    }

    fun getStatus(mediaItem: MediaItem): Int? {
        val download = downloads[Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        return download?.state
    }


    fun getDownloadRequest(uri: Uri): DownloadRequest? {
        return downloads[uri]?.request
    }


    fun getDownloadHelper (mediaItem: MediaItem): DownloadHelper {
        return DownloadHelper.forMediaItem(
            context, mediaItem, buildRenderersFactory(
                context, true
            ), dataSourceFactory
        )
    }
     fun buildDownloadRequest(downloadHelper: DownloadHelper, mediaItem: MediaItem): DownloadRequest {

        return downloadHelper
            .getDownloadRequest(
                Util.getUtf8Bytes(Preconditions.checkNotNull<String>(mediaItem.mediaMetadata.title.toString()))
            )

    }




    fun addDownloadRequest(downloadRequest: DownloadRequest ){
        DownloadService.sendAddDownload(
            context,
            com.musify.app.player.DownloadService::class.java,
            downloadRequest,  /* foreground= */
            false
        )
    }

    fun deleteDownloadRequest(downloadRequest: DownloadRequest ){


        DownloadService.sendRemoveDownload(
            context,
            com.musify.app.player.DownloadService::class.java,
            downloadRequest.id,  /* foreground= */
            false
        )
    }




    fun getStartDownloadDialogHelper(mediaItem: MediaItem): StartDownloadDialogHelper? {
        startDownloadDialogHelper?.release()
        startDownloadDialogHelper = StartDownloadDialogHelper(
            DownloadHelper.forMediaItem(
                context,
                mediaItem,
                buildRenderersFactory(context, true),
                dataSourceFactory
            ),
            mediaItem
        )

        return startDownloadDialogHelper
    }

    fun download(
        mediaItem: MediaItem,
    ) {

        val download = downloads[Preconditions.checkNotNull(mediaItem.localConfiguration).uri]
        Log.e(TAG, "toggleDownload: " + download)
        if (download != null && download.state != Download.STATE_FAILED) {
//            DownloadService.sendRemoveDownload(
//                context,
//                DownloadService::class.java,
//                download.request.id,  /* foreground= */
//                false
//            )
        } else {
//            startDownloadDialogHelper?.release()
            startDownloadDialogHelper = StartDownloadDialogHelper(
                DownloadHelper.forMediaItem(
                    context,
                    mediaItem,
                    buildRenderersFactory(context, true),
                    dataSourceFactory
                ),
                mediaItem
            )


        }
    }

    private fun loadDownloads() {
        try {
            downloadIndex!!.getDownloads().use { loadedDownloads ->
                while (loadedDownloads.moveToNext()) {
                    val download = loadedDownloads.download
                    downloads[download.request.uri] = download
                }
            }
        } catch (e: IOException) {
            Log.w(TAG, "Failed to query downloads", e)
        }
    }

    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager, download: Download, finalException: Exception?
        ) {

            downloads[download.request.uri] = download
            for (listener in listeners) {
                listener.onDownloadsChanged()
            }
        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            downloads.remove(download.request.uri)
            for (listener in listeners) {
                listener.onDownloadsChanged()
            }
        }
    }

    inner class StartDownloadDialogHelper(
        private val downloadHelper: DownloadHelper,
        private val mediaItem: MediaItem
    ) : DownloadHelper.Callback, /*TrackSelectionDialog.TrackSelectionListener,*/
        DialogInterface.OnDismissListener {
        //        private var trackSelectionDialog: TrackSelectionDialog? = null
        private var widevineOfflineLicenseFetchTask: DownloadTracker.WidevineOfflineLicenseFetchTask? =
            null
        private var keySetId: ByteArray? = null

        init {
            Log.e("TAG", "PREPARE")
            downloadHelper.prepare(this)
        }


        fun release() {
            downloadHelper.release()
//            if (trackSelectionDialog != null) {
//                trackSelectionDialog.dismiss()
//            }
            if (widevineOfflineLicenseFetchTask != null) {
                widevineOfflineLicenseFetchTask?.cancel(false)
            }
        }

        // DownloadHelper.Callback implementation.
        override fun onPrepared(helper: DownloadHelper) {
            val format = getFirstFormatWithDrmInitData(helper)
            Log.e(TAG, "onPrepared toggleDownload: " + format)

            if (format == null) {
                onDownloadPrepared(helper)
                return
            }

//             The content is DRM protected. We need to acquire an offline license.
//            if (Util.SDK_INT < 18) {
//                Toast.makeText(
//                    context,
//                    R.string.error_drm_unsupported_before_api_18,
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                Log.e(
//                    "DownloadTracker.TAG",
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
//                    "DownloadTracker.TAG",
//                    "Downloading content where DRM scheme data is not located in the manifest is not"
//                            + " supported"
//                )
//                return
//            }
//            widevineOfflineLicenseFetchTask =
//                mediaItem.localConfiguration?.drmConfiguration?.let {
//                    WidevineOfflineLicenseFetchTask(
//                        format,
//                        it,
//                        dataSourceFactory,  /* dialogHelper= */
//                        this,
//                        helper
//                    )
//                }
//            widevineOfflineLicenseFetchTask?.execute()
        }

        override fun onPrepareError(helper: DownloadHelper, e: IOException) {
            val isLiveContent = e is LiveContentUnsupportedException
            val toastStringId: Int =
                if (isLiveContent) R.string.download_live_unsupported else R.string.download_start_error
            val logMessage =
                if (isLiveContent) "Downloading live content unsupported" else "Failed to start download"
            Toast.makeText(context, toastStringId, Toast.LENGTH_LONG).show()
            Log.e("DownloadTracker.TAG", logMessage, e)
        }

        // TrackSelectionListener implementation.
        fun onTracksSelected(trackSelectionParameters: TrackSelectionParameters?) {
            for (periodIndex in 0 until downloadHelper.periodCount) {
                downloadHelper.clearTrackSelections(periodIndex)
                downloadHelper.addTrackSelection(periodIndex, trackSelectionParameters!!)
            }
            val downloadRequest = buildDownloadRequest()
            if (downloadRequest.streamKeys.isEmpty()) {
                // All tracks were deselected in the dialog. Don't start the download.
                return
            }
            startDownload(downloadRequest)
        }



        // DialogInterface.OnDismissListener implementation.
        override fun onDismiss(dialogInterface: DialogInterface) {
//            trackSelectionDialog = null
            downloadHelper.release()
        }
        // Internal methods.
        /**
         * Returns the first [Format] with a non-null [Format.drmInitData] found in the
         * content's tracks, or null if none is found.
         */
        fun getFirstFormatWithDrmInitData(helper: DownloadHelper): Format? {
            for (periodIndex in 0 until helper.periodCount) {
                val mappedTrackInfo = helper.getMappedTrackInfo(periodIndex)
                for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                    val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
                    for (trackGroupIndex in 0 until trackGroups.length) {
                        val trackGroup = trackGroups[trackGroupIndex]
                        for (formatIndex in 0 until trackGroup.length) {
                            val format = trackGroup.getFormat(formatIndex)
                            if (format.drmInitData != null) {
                                return format
                            }
                        }
                    }
                }
            }
            return null
        }

        fun onOfflineLicenseFetched(helper: DownloadHelper, keySetId: ByteArray) {
            this.keySetId = keySetId
            onDownloadPrepared(helper)
        }

        fun onOfflineLicenseFetchedError(e: DrmSessionException) {
            Toast.makeText(
                context,
                R.string.download_start_error_offline_license,
                Toast.LENGTH_LONG
            )
                .show()
            Log.e("DownloadTracker.TAG", "Failed to fetch offline DRM license", e)
        }

        private fun onDownloadPrepared(helper: DownloadHelper) {
            android.util.Log.e(TAG, "onDownloadPrepared: ")
            if (helper.periodCount == 0) {
                Log.d("DownloadTracker.TAG", "No periods found. Downloading entire stream.")
                startDownload()
                downloadHelper.release()
                return
            }
            val tracks = downloadHelper.getTracks( /* periodIndex= */0)
            createForTracksAndParameters(
                /* titleId= */
                R.string.exo_download_description,
                tracks,
                DownloadHelper.getDefaultTrackSelectorParameters(context),  /* allowAdaptiveSelections= */
                false,  /* allowMultipleOverrides= */
                true,  /* onTracksSelectedListener= */
                this

            )

        }

        /**
         * Returns whether any [DrmInitData.SchemeData] that [ ][DrmInitData.SchemeData.matches] [C.WIDEVINE_UUID] has non-null [ ][DrmInitData.SchemeData.data].
         */
        private fun hasNonNullWidevineSchemaData(drmInitData: DrmInitData?): Boolean {
            for (i in 0 until drmInitData!!.schemeDataCount) {
                val schemeData = drmInitData[i]
                if (schemeData.matches(C.WIDEVINE_UUID) && schemeData.hasData()) {
                    return true
                }
            }
            return false
        }

        private fun startDownload(downloadRequest: DownloadRequest = buildDownloadRequest()) {
            DownloadService.sendAddDownload(
                context,
                com.musify.app.player.DownloadService::class.java,
                downloadRequest,  /* foreground= */
                false
            )

        }

        private fun buildDownloadRequest(): DownloadRequest {
            return downloadHelper
                .getDownloadRequest(
                    Util.getUtf8Bytes(
                        Preconditions.checkNotNull(
                            mediaItem.mediaMetadata.title.toString()
                        )
                    )
                )
                .copyWithKeySetId(keySetId)
        }
    }

    /** Downloads a Widevine offline license in a background thread.  */
    private inner class WidevineOfflineLicenseFetchTask(
        private val format: Format,
        private val drmConfiguration: DrmConfiguration,
        private val dataSourceFactory: DataSource.Factory,
        private val dialogHelper: StartDownloadDialogHelper,
        private val downloadHelper: DownloadHelper
    ) : AsyncTask<Void?, Void?, Void?>() {
        private var keySetId: ByteArray? = null
        private var drmSessionException: DrmSessionException? = null

        override fun onPostExecute(aVoid: Void?) {
            if (drmSessionException != null) {
                dialogHelper.onOfflineLicenseFetchedError(drmSessionException!!)
            } else {
                dialogHelper.onOfflineLicenseFetched(
                    downloadHelper,
                    Preconditions.checkNotNull(keySetId)
                )
            }
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val offlineLicenseHelper = OfflineLicenseHelper.newWidevineInstance(
                drmConfiguration.licenseUri.toString(),
                drmConfiguration.forceDefaultLicenseUri,
                dataSourceFactory,
                drmConfiguration.licenseRequestHeaders,
                DrmSessionEventListener.EventDispatcher()
            )
            try {
                keySetId = offlineLicenseHelper.downloadLicense(format)
            } catch (e: DrmSessionException) {
                drmSessionException = e
            } finally {
                offlineLicenseHelper.release()
            }
            return null
        }
    }


    val SUPPORTED_TRACK_TYPES =
        ImmutableList.of(C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO, C.TRACK_TYPE_TEXT)


    fun createForTracksAndParameters(
        titleId: Int,
        tracks: Tracks?,
        trackSelectionParameters: TrackSelectionParameters,
        allowAdaptiveSelections: Boolean,
        allowMultipleOverrides: Boolean,
        startDownloadDialogHelper: StartDownloadDialogHelper,
    ) {

        val trackGroups = ArrayList<Tracks.Group>()
        for (i in SUPPORTED_TRACK_TYPES.indices) {
            val trackType: @TrackType Int = SUPPORTED_TRACK_TYPES.get(i)

            for (trackGroup in tracks!!.groups) {
                if (trackGroup.type == trackType) {
                    trackGroups.add(trackGroup)
                }
            }

        }
        val builder = trackSelectionParameters.buildUpon()
        for (i in SUPPORTED_TRACK_TYPES.indices) {
            val trackType: Int = SUPPORTED_TRACK_TYPES.get(i)
            builder.setTrackTypeDisabled(
                trackType,
                false
            )

            builder.clearOverridesOfType(trackType)
            val overrides: Map<TrackGroup, TrackSelectionOverride> =
                java.util.HashMap<TrackGroup, TrackSelectionOverride>(
                    TrackSelectionView.filterOverrides(
                        trackSelectionParameters.overrides,
                        trackGroups,
                        allowMultipleOverrides
                    )
                );
            for (override in overrides.values) {
                builder.addOverride(override)
            }
        }
        startDownloadDialogHelper.onTracksSelected(builder.build())


    }

    fun filterOverrides(
        overrides: Map<TrackGroup?, TrackSelectionOverride?>,
        trackGroups: List<Tracks.Group>,
        allowMultipleOverrides: Boolean
    ): Map<TrackGroup, TrackSelectionOverride>? {
        val filteredOverrides = HashMap<TrackGroup, TrackSelectionOverride>()
        for (i in trackGroups.indices) {
            val trackGroup = trackGroups[i]
            val override = overrides[trackGroup.mediaTrackGroup]
            if (override != null && (allowMultipleOverrides || filteredOverrides.isEmpty())) {
                filteredOverrides[override.mediaTrackGroup] = override
            }
        }
        return filteredOverrides
    }

}