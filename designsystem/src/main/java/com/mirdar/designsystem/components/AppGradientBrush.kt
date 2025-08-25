package com.mirdar.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import com.mirdar.designsystem.theme.VideoDownloaderTheme

object AppDefaults {

    val brush: Brush
        @Composable get() = Brush.linearGradient(
            colors = listOf(
                VideoDownloaderTheme.colors.blue.copy(alpha = 0.7f),
                VideoDownloaderTheme.colors.primary
            )
        )
}
