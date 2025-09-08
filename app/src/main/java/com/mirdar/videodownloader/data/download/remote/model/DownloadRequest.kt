package com.mirdar.videodownloader.data.download.remote.model

import android.net.Uri
import com.mirdar.videodownloader.data.download.cache.model.CacheDownload

data class DownloadRequest(
    val id: String,
    val url: String,
    val destination: Uri,
    val thumbnail: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
)

fun DownloadRequest.toDbModel() = CacheDownload(
    id = id,
    thumbnail = thumbnail,
    title = title,
    description = description,
    isCompleted = isCompleted
)