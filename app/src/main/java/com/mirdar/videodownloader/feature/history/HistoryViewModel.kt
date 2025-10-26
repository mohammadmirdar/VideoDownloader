package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.GetDownloadStateUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.GetDownloadStatusUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history.model.HistoryUiState
import com.mirdar.videodownloader.feature.home.uriExists
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDownloadState: GetDownloadStateUseCase,
    private val getDownloadStatus: GetDownloadStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state = _state.asStateFlow()

    init {
        observeDownloadState()
    }

    private fun observeDownloadState() {
        getDownloadState()
            .onEach { state ->
                _state.update {
                    HistoryUiState(
                        downloads = state.items
                            .filter { item -> item.status == DownloadStatus.Completed }
                            .filter { item -> context.uriExists(item.fileUri.toUri()) }
                            .toImmutableList()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onItemClicked(item: DownloadItem) {
        if (context.uriExists(item.fileUri.toUri())) {
            openExternalPlayer(item.fileUri)
        }
    }

    private fun openExternalPlayer(fileUri: String) {
        val uri = fileUri.toUri()

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }
}