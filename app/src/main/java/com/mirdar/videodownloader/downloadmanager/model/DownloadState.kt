package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DownloadsState(
    val items: ImmutableList<DownloadItem> = persistentListOf(),
    val lastUpdate: Long = System.currentTimeMillis()
)