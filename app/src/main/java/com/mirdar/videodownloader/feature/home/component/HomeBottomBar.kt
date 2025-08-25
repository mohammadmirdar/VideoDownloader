package com.mirdar.videodownloader.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.R

@Composable
internal fun HomeBottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(
        modifier = modifier,
        containerColor = VideoDownloaderTheme.colors.white,
        contentColor = VideoDownloaderTheme.colors.gray
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                70.dp, alignment = Alignment.CenterHorizontally
            )
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )
        }
    }
}