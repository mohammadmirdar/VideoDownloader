package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model

import com.mirdar.videodownloader.com.mirdar.videodownloader.util.com.mirdar.videodownloader.util.DownloaderSnackUiEvent

sealed class HomeUiEvents {

    data class Message(val snackBar: DownloaderSnackUiEvent.Snackbar) : HomeUiEvents()
    data object NavigateToDownload: HomeUiEvents()
    data object HideToast: HomeUiEvents()

    data class Toast(val snackBar: DownloaderSnackUiEvent.Snackbar) : HomeUiEvents()
}