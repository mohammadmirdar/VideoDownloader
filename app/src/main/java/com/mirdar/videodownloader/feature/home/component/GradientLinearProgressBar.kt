package com.mirdar.videodownloader.feature.home.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.components.AppDefaults

@Composable
fun GradientLinearProgressBar(
    progress: Int, // value from 0 .. 100
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f),
    height: Dp = 6.dp,
    shape: Shape = RoundedCornerShape(50) // pill shaped
) {
    val maxWidthValue by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween()
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = maxWidthValue.coerceIn(0f, 1f))
                .background(AppDefaults.brush)
        )
    }
}
