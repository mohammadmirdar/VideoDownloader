package com.mirdar.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = VideoDownloaderTheme.colors.gray
) {
    Text(
        modifier = modifier,
        text = text,
        style = style.copy(brush = AppDefaults.brush),
        color = color
    )
}