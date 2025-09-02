package com.mirdar.videodownloader.data.download.model

import android.net.Uri

data class DownloadRequest(
    val id: DownloadId,
    val url: String,
    val destination: Uri, // SAF or MediaStore URI
    val suggestedFilename: String? = null,
    val resumeIfPossible: Boolean = true,
)

@JvmInline
value class DownloadId(val value: String)