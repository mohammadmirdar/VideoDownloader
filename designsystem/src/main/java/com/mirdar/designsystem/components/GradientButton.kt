package com.mirdar.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@Composable
fun GradientButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundModifier = if (isSelected) {
        modifier.background(
            brush = AppDefaults.brush,
            shape = ButtonDefaults.shape
        )
    } else {
        modifier.background(
            color = VideoDownloaderTheme.colors.lightGray,
            shape = ButtonDefaults.shape
        )
    }
    Button(
        modifier = backgroundModifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = text,
            color = if (isSelected) VideoDownloaderTheme.colors.white else VideoDownloaderTheme.colors.gray,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
            maxLines = 1
        )
    }
}

@Composable
@Preview

private fun GradientButtonPreview(
    @PreviewParameter(provider = ButtonButtonPreviewParameters::class) isSelected: Boolean
) {
    VideoDownloaderTheme {
        GradientButton(
            text = "Most Viewed",
            isSelected = isSelected,
            onClick = {}
        )
    }
}

private class ButtonButtonPreviewParameters : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)

}
