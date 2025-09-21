package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history.model

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HistoryUiState(
    val downloads: ImmutableList<DownloadItem> = persistentListOf(),
    val isLoading: Boolean = false,
    val homeError: HistoryError = HistoryError.None
)

