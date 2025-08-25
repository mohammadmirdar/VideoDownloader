package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mirdar.designsystem.theme.VideoDownloaderTheme

@Composable
fun LatestDownloadList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = VideoDownloaderTheme.colors.lightGray, shape = RoundedCornerShape(15.dp)
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            (0..2).forEach {_->
                DownloadItem()
                HorizontalDivider(thickness = 2.dp, color = VideoDownloaderTheme.colors.gray)
            }

            ViewAllItem()
        }
    }
}

@Composable
private fun ViewAllItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "View All",
            style = MaterialTheme.typography.bodyLarge,
            color = VideoDownloaderTheme.colors.black
        )
    }
}

@Composable
private fun DownloadItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally)
    ) {
        AsyncImage(
            model = "",
            contentDescription = null,
            modifier = Modifier
                .height(60.dp)
                .aspectRatio(1.618f)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Downloading...",
                style = MaterialTheme.typography.bodyMedium,
                color = VideoDownloaderTheme.colors.black
            )

            GradientLinearProgressBar(progress = 0.75f, modifier = Modifier.padding(end = 18.dp))
        }

        VerticalDivider(
            Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp)),
            thickness = 2.dp,
            color = VideoDownloaderTheme.colors.darkGray
        )

        Icon(
            Icons.Default.Clear,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = VideoDownloaderTheme.colors.darkGray
        )
    }
}