package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LatestDownloadList(
    downloadList: ImmutableList<DownloadItem>,
    onViewAllClick: () -> Unit,
    showButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = VideoDownloaderTheme.colors.lightGray, shape = RoundedCornerShape(15.dp)
            )
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = downloadList,
                key = { key -> key.id }
            ) {
                DownloadItem(downloadItem = it)
                HorizontalDivider(thickness = 2.dp, color = VideoDownloaderTheme.colors.gray)
            }

            if (showButton) {
                item {
                    ViewAllItem(onClick = onViewAllClick)
                }
            }
        }
    }
}

@Composable
private fun ViewAllItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "View All",
            style = MaterialTheme.typography.bodyLarge,
            color = VideoDownloaderTheme.colors.black
        )
    }
}

@Composable
private fun DownloadItem(downloadItem: DownloadItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally)
    ) {
        AsyncImage(
            model = downloadItem.poster,
            contentDescription = null,
            modifier = Modifier
                .height(50.dp)
                .width(80.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = downloadItem.title,
                style = MaterialTheme.typography.bodyMedium,
                color = VideoDownloaderTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (downloadItem.status != DownloadStatus.Completed) {
                GradientLinearProgressBar(
                    progress = downloadItem.progress,
                    modifier = Modifier.padding(end = 18.dp)
                )
            }
        }

        if (downloadItem.status != DownloadStatus.Completed) {
            VerticalDivider(
                Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp)),
                thickness = 2.dp,
                color = VideoDownloaderTheme.colors.darkGray
            )

            val imageVector = when (downloadItem.status) {
                DownloadStatus.Failed -> {
                    Icons.Default.Refresh
                }

                DownloadStatus.Paused -> {
                    Icons.Default.PlayArrow
                }

                else -> {
                    Icons.Default.Clear
                }
            }

            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = VideoDownloaderTheme.colors.darkGray
            )
        }


    }
}