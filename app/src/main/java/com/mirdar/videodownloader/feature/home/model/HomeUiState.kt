package com.mirdar.videodownloader.feature.home.model

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val videoInfo: VideoInfo = VideoInfo(),
    val downloads: ImmutableList<DownloadItem> = persistentListOf()
)

data class VideoInfo(
    val description: String = "",
    val directUrl: String = "",
    val thumbnail: String = "",
    val title: String = ""
)