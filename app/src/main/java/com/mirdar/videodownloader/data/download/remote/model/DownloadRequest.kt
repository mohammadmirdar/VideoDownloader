package com.mirdar.videodownloader.data.download.remote.model

import android.net.Uri

data class DownloadRequest(
    val id: String,
    val url: String,
    val destination: Uri,
    val suggestedFilename: String? = null,
    val resumeIfPossible: Boolean = true,
)