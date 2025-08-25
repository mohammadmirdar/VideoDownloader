package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.layout.size
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
import com.mirdar.videodownloader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier, colors = topAppBarColors(
            containerColor = VideoDownloaderTheme.colors.white
        ), title = {
            Text(
                "Video Downloader",
                style = MaterialTheme.typography.headlineSmall,
                color = VideoDownloaderTheme.colors.black
            )
        }, actions = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )
        })
}