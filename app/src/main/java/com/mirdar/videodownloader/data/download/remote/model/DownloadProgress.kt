package com.mirdar.videodownloader.data.download.remote.model


data class DownloadProgress(
    val id: String,
    val bytesDownloaded: Long,
    val totalBytes: Long?, // null if unknown
) {
    val percent: Int? =
        totalBytes?.let { if (it > 0) ((bytesDownloaded * 100L) / it).toInt() else null }
}