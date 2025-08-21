@file:Suppress("CompositionLocalAllowlist")

package com.mirdar.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalVideoDownloaderColors = staticCompositionLocalOf {
    VideoDownloaderColors()
}

@Composable
fun VideoDownloaderTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
         LocalVideoDownloaderColors provides VideoDownloaderColors()
    ) {
        MaterialTheme(
            typography = Typography,
            colorScheme = darkColorScheme(surface = VideoDownloaderTheme.colors.black),
            content = content
        )
    }
}

object VideoDownloaderTheme {
    val colors: VideoDownloaderColors
        @Composable
        get() = LocalVideoDownloaderColors.current
}
