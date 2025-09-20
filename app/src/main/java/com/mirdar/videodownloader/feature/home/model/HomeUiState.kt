package com.mirdar.videodownloader.feature.home.model

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model.HomeError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val videoInfo: VideoInfo = VideoInfo(),
    val downloads: ImmutableList<DownloadItem> = persistentListOf(),
    val copiedText: String = "",
    val isLoading: Boolean = false,
    val homeError: HomeError = HomeError.None
)

data class VideoInfo(
    val description: String = "",
    val directUrl: String = "",
    val thumbnail: String = "",
    val title: String = ""
)