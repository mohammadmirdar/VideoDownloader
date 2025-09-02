package com.mirdar.videodownloader.data.download.model

data class DownloadProgress(
    val id: DownloadId,
    val bytesDownloaded: Long,
    val totalBytes: Long?, // null if unknown
) {
    val percent: Int? =
        totalBytes?.let { if (it > 0) ((bytesDownloaded * 100L) / it).toInt() else null }
}