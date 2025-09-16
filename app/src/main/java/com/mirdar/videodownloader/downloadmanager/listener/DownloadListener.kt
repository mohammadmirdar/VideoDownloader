package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.listener

import android.net.Uri
import androidx.core.net.toUri
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener

class DownloadListener(
    private val onUpdate: (Download, Boolean) -> Unit,
    private val onFinalize: (Uri?) -> Unit,
) : FetchListener {

    override fun onAdded(download: Download) = onUpdate(download, false)
    override fun onCompleted(download: Download) {
        onUpdate(download, false)
        onFinalize(download.file.toUri())
    }

    override fun onDeleted(download: Download) = onUpdate(download, false)

    override fun onError(
        download: Download,
        error: Error,
        throwable: Throwable?
    ) = onUpdate(download, false)

    override fun onPaused(download: Download) = onUpdate(download, false)
    override fun onProgress(
        download: Download,
        etaInMilliSeconds: Long,
        downloadedBytesPerSecond: Long
    ) =
        onUpdate(download, false)

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) =
        onUpdate(download, waitingOnNetwork)

    override fun onRemoved(download: Download) = onUpdate(download, false)
    override fun onResumed(download: Download) = onUpdate(download, false)
    override fun onStarted(
        download: Download,
        downloadBlocks: List<com.tonyodev.fetch2core.DownloadBlock>,
        totalBlocks: Int
    ) =
        onUpdate(download, false)

    override fun onWaitingNetwork(download: Download) = onUpdate(download, true)

    override fun onCancelled(download: Download) = onUpdate(download, false)
    override fun onDownloadBlockUpdated(
        download: Download,
        downloadBlock: com.tonyodev.fetch2core.DownloadBlock,
        totalBlocks: Int
    ) {
    }
}