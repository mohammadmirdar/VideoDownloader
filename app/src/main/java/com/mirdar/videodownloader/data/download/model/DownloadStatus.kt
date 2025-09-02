package com.mirdar.videodownloader.data.download.model

import android.net.Uri

sealed interface DownloadStatus {
    data class Started(val id: DownloadId, val totalBytes: Long?) : DownloadStatus
    data class Progress(val progress: DownloadProgress) : DownloadStatus
    data class Completed(val id: DownloadId, val uri: Uri) : DownloadStatus
    data class Failed(val id: DownloadId, val error: Throwable) : DownloadStatus
    data class Cancelled(val id: DownloadId) : DownloadStatus
}