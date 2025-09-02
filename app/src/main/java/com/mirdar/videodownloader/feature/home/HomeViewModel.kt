package com.mirdar.videodownloader.feature.home

import android.content.Context
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.adivery.sdk.Adivery
import com.adivery.sdk.AdiveryListener
import com.mirdar.videodownloader.data.download.remote.model.DownloadRequest
import com.mirdar.videodownloader.domain.download.usecase.CancelDownloadUseCase
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import com.mirdar.videodownloader.domain.home.usecase.GetVideoInfoUseCase
import com.mirdar.videodownloader.feature.home.model.HomeUiState
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.service.DownloadService
import com.mirdar.videodownloader.util.NetworkState
import com.mirdar.videodownloader.util.State
import com.mirdar.videodownloader.util.toError
import com.mirdar.videodownloader.util.toSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appConfig: AppConfig,
    private val requestVideoInfo: GetVideoInfoUseCase,
    private val cancelDownload: CancelDownloadUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<NetworkState<HomeUiState>>(State.Loading())
    val state = _state.asStateFlow()

    var currentRequestedVideo: String = ""

    init {
        listenOnAdivery()
    }

    private fun listenOnAdivery() {
        Adivery.addGlobalListener(object : AdiveryListener() {
            override fun onRewardedAdClosed(placementId: String, isRewarded: Boolean) {
                if (isRewarded) {
                }
            }
        })
    }

    fun getVideoInfo(url: String) {
        viewModelScope.launch {
            requestVideoInfo(url)
                .onRight(::onRightVideoInfo)
                .onLeft(::onLeftVideoInfo)
        }
    }

    fun onDownloadClicked(videoUrl: String) {
        getVideoInfo(url = currentRequestedVideo)

        currentRequestedVideo = videoUrl
    }

    private fun onRightVideoInfo(videoInfoModel: VideoInfoModel) {
        startDownload(
            context = context,
            videoUrl = videoInfoModel.directUrl
        )

        _state.value = HomeUiState(
            description = videoInfoModel.description,
            directUrl = videoInfoModel.directUrl,
            thumbnail = videoInfoModel.thumbnail,
            title = videoInfoModel.title
        ).toSuccess()
    }

    private fun onLeftVideoInfo(callError: CallError) {
        _state.value = callError.toError()
    }

    private fun startDownload(context: Context, videoUrl: String) {
        val id = System.currentTimeMillis().toString()

        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (downloadsDir == null) {
            return
        }

        val fileName = "download_${System.currentTimeMillis()}.bin"
        val file = File(downloadsDir, fileName)

        // Get a Uri via FileProvider for SAF-compatible access
        val destinationUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val request = DownloadRequest(
            id = id,
            url = videoUrl,
            destination = destinationUri
        )

        DownloadService.start(context, request)
    }
}