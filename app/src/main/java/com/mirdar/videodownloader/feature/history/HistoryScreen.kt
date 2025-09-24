package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history.model.HistoryUiState
import com.mirdar.videodownloader.feature.home.component.LatestDownloadList

@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val state = historyViewModel.state.collectAsStateWithLifecycle()

    HistoryContent(
        state = state.value
    )
}

@Composable
private fun HistoryContent(
    state: HistoryUiState
){
    if (state.downloads.isNotEmpty()) {
        LatestDownloadList(
            downloadList = state.downloads,
            modifier = Modifier.fillMaxSize(),
            onViewAllClick = {},
            showButton = false,
            onItemClick = {}
        )
    }
}