package com.mirdar.videodownloader.feature.home

import android.view.LayoutInflater
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adivery.sdk.AdiveryNativeAdView
import com.mirdar.designsystem.components.GradientButton
import com.mirdar.designsystem.components.GradientOutlineButton
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.R
import com.mirdar.videodownloader.com.mirdar.videodownloader.util.onSuccess
import com.mirdar.videodownloader.feature.home.component.HomeBottomBar
import com.mirdar.videodownloader.feature.home.component.HomeTopBar
import com.mirdar.videodownloader.feature.home.component.LatestDownloadList
import com.mirdar.videodownloader.feature.home.model.HomeUiState

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    Scaffold(containerColor = VideoDownloaderTheme.colors.white,
        topBar = {
        HomeTopBar(modifier = Modifier)
    }, bottomBar = {
        HomeBottomBar()
    }) { paddingValues ->
        state.onSuccess {
            HomeContent(
                uiState = it,
                onDownloadClick = homeViewModel::onDownloadClicked,
                onPasteClick = {},
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onDownloadClick: (String) -> Unit,
    onPasteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf("") }
    var nativeAdView: AdiveryNativeAdView? by remember { mutableStateOf(null) }

    LaunchedEffect(nativeAdView) {
        nativeAdView?.loadAd()
    }

    Column(
        modifier = modifier.padding(horizontal = 26.dp)
    ) {
        Spacer(Modifier.height(26.dp))

        OutlinedTextField(
            value = textValue, onValueChange = { textValue = it }, placeholder = {
                Text(
                    text = stringResource(R.string.paste_link_here)
                )
            },
            shape = RoundedCornerShape(size = 20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = VideoDownloaderTheme.colors.white,
                unfocusedBorderColor = VideoDownloaderTheme.colors.gray,
                focusedTextColor = VideoDownloaderTheme.colors.black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        ButtonRow(
            onDownloadClick = { onDownloadClick(textValue) },
            onPasteClick = onPasteClick
        )

        Spacer(Modifier.height(42.dp))

        LatestDownloadList(downloadList = uiState.downloads)

        Spacer(Modifier.height(45.dp))

        AndroidView(
            factory = {
                val addView = LayoutInflater.from(it)
                    .inflate(R.layout.adivery_native_ad_view, null, false) as AdiveryNativeAdView
                nativeAdView = addView
                addView
            },
            modifier = Modifier.fillMaxSize()
        )

    }
}

@Composable
fun ButtonRow(
    onDownloadClick: () -> Unit,
    onPasteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        GradientOutlineButton(
            onClick = onPasteClick,
            text = stringResource(R.string.paste_link),
            modifier = Modifier.weight(1f)
        )

        GradientButton(
            text = stringResource(R.string.download),
            isSelected = true,
            onClick = onDownloadClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview(device = "id:pixel_8_pro")
private fun HomeScreenPreview() {
    VideoDownloaderTheme {
        HomeScreen()
    }
}