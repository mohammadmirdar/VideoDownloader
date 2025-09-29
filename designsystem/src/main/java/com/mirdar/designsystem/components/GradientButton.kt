package com.mirdar.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@Composable
fun GradientButton(
    text: String,
    isSelected: Boolean,
    isLoading: Boolean,
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
        ),
        enabled = isLoading.not()
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(35.dp),
                    color = VideoDownloaderTheme.colors.white,
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 2.dp,
                )
            }
        } else {
            Text(
                text = text,
                color = if (isSelected) VideoDownloaderTheme.colors.white else VideoDownloaderTheme.colors.gray,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                maxLines = 1
            )
        }
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
            isLoading = true,
            onClick = {}
        )
    }
}

private class ButtonButtonPreviewParameters : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}
