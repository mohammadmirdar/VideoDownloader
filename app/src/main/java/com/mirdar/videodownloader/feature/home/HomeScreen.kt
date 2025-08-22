package com.mirdar.videodownloader.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mirdar.designsystem.components.GradientButton
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.R

@Composable
fun HomeScreen() {
    Scaffold(containerColor = VideoDownloaderTheme.colors.white, topBar = {
        HomeTopBar(modifier = Modifier)
    }, bottomBar = {
        HomeBottomBar()
    }) { paddingValues ->
        HomeContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 26.dp)
    ) {
        Text(
            text = "Download Video from everywhere", color = VideoDownloaderTheme.colors.darkGray
        )

        Spacer(Modifier.height(38.dp))

        OutlinedTextField(
            value = "", onValueChange = { }, placeholder = {
                Text(
                    text = "Paste Link here"
                )
            }, shape = RoundedCornerShape(size = 20.dp), colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = VideoDownloaderTheme.colors.white,
                unfocusedBorderColor = VideoDownloaderTheme.colors.gray
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(42.dp))

        LatestDownloadsTitle()

        Spacer(Modifier.height(40.dp))

        Tabs()

        Spacer(Modifier.height(45.dp))

        HomePager()
    }
}

@Composable
private fun HomePager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    HorizontalPager(
        modifier = modifier.padding(18.dp),
        state = pagerState
    ) { page ->
        PagerItem()
    }
}

@Composable
private fun PagerItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 32.dp))
            .background(
                color = VideoDownloaderTheme.colors.lightGray,
                shape = RoundedCornerShape(size = 32.dp)
            )
            .aspectRatio(ratio = 0.66f)
            .padding(14.dp)
    ) {
        Icon(
            painter = painterResource(com.mirdar.designsystem.R.drawable.ic_heart),
            contentDescription = null,
            tint = VideoDownloaderTheme.colors.white,
            modifier = Modifier
                .size(44.dp)
                .background(color = VideoDownloaderTheme.colors.darkGray, shape = CircleShape)
                .align(Alignment.TopEnd)
                .padding(10.dp)
        )
    }
}

@Composable
private fun LatestDownloadsTitle(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Latest Downloads", style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "View all",
            style = MaterialTheme.typography.bodyLarge,
            color = VideoDownloaderTheme.colors.gray
        )
    }
}

@Composable
private fun Tabs(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GradientButton(
            text = "Most Viewed", onClick = {}, modifier = Modifier.height(54.dp), isSelected = true
        )
        GradientButton(
            text = "Nearby", onClick = {}, modifier = Modifier.height(54.dp), isSelected = false
        )
        GradientButton(
            text = "Latest", onClick = {}, modifier = Modifier.height(54.dp), isSelected = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(modifier: Modifier = Modifier) {
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

@Composable
private fun HomeBottomBar(modifier: Modifier = Modifier) {
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

@Composable
@Preview(device = "id:pixel_8_pro")
private fun HomeScreenPreview() {
    VideoDownloaderTheme {
        HomeScreen()
    }
}