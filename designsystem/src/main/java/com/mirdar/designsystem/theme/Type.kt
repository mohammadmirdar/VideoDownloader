package com.mirdar.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mirdar.designsystem.R

val samimFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.samim,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.samim_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.samim_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )
)

val Typography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 42.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)
