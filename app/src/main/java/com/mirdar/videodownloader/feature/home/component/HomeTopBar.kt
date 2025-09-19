package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier, colors = topAppBarColors(
            containerColor = VideoDownloaderTheme.colors.white
        ), title = {
            Text(
                "Video Downloader",
                style = MaterialTheme.typography.titleLarge,
                color = VideoDownloaderTheme.colors.black
            )
        }, actions = {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(com.mirdar.designsystem.R.drawable.ic_setting_fill),
                contentDescription = null,
                tint = VideoDownloaderTheme.colors.black
            )
            Spacer(Modifier.width(8.dp))
        })
}