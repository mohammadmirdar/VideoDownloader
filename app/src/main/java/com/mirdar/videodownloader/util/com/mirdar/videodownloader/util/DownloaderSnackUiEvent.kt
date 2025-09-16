package com.mirdar.videodownloader.com.mirdar.videodownloader.util.com.mirdar.videodownloader.util

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

sealed class DownloaderSnackUiEvent {
    data class Snackbar(
        override val message: String,
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = false,
        override val duration: SnackbarDuration = if (actionLabel == null) {
            SnackbarDuration.Short
        } else {
            SnackbarDuration.Indefinite
        },
        val isError: Boolean = false,
        @DrawableRes val icon: Int? = null,
        val callAction: (() -> Unit)? = null,
        val type: MessageType = MessageType.SNACK,
    ) : DownloaderSnackUiEvent(), SnackbarVisuals

    enum class MessageType {
        SNACK, SNACK_FLOAT, FLOAT
    }
}
