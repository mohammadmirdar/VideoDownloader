package com.mirdar.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@Composable
fun GradientOutlineButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    // Define a shape for the border and the Button itself.
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .border(
                brush = AppDefaults.brush,
                width = 1.dp,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            GradientText(text)
        }
    }
}

@Composable
@Preview
fun GradientOutlinedTextFieldPreview() {
    VideoDownloaderTheme {
        GradientOutlineButton(
            text = "Pase link",
            onClick = {}
        )
    }
}