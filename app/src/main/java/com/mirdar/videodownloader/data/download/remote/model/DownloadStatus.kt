package com.mirdar.videodownloader.data.download.remote.model

import android.net.Uri
import com.mirdar.videodownloader.data.download.remote.model.DownloadProgress

sealed interface DownloadStatus {
    data class Started(val id: String, val totalBytes: Long?) : DownloadStatus
    data class Progress(val progress: DownloadProgress) : DownloadStatus
    data class Completed(val id: String, val uri: Uri) : DownloadStatus
    data class Failed(val id: String, val error: Throwable) : DownloadStatus
    data class Cancelled(val id: String) : DownloadStatus
}