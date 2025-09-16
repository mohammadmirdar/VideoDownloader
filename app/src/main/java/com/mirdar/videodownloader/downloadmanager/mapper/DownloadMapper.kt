package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.mapper

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.squareup.moshi.JsonAdapter
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Status

object DownloadMapper {

    private fun mapStatus(status: Status, isWaitingForNetwork: Boolean = false): DownloadStatus =
        when {
            isWaitingForNetwork -> DownloadStatus.Network
            status == Status.QUEUED -> DownloadStatus.Queued
            status == Status.DOWNLOADING -> DownloadStatus.Downloading
            status == Status.PAUSED -> DownloadStatus.Paused
            status == Status.COMPLETED -> DownloadStatus.Completed
            status == Status.FAILED -> DownloadStatus.Failed
            status == Status.DELETED || status == Status.CANCELLED -> DownloadStatus.Delete
            else -> DownloadStatus.Init
        }

    fun mapDownload(
        download: Download,
        adapter: JsonAdapter<DownloadItem>,
        isWaitingForNetwork: Boolean = false,
    ): DownloadItem? {
        return download.tag?.let { tag ->
            adapter.fromJson(tag)?.copy(
                id = download.id,
                status = mapStatus(download.status, isWaitingForNetwork),
                progress = download.progress,
                lastUpdateTime = System.currentTimeMillis()
            )
        }
    }
}
