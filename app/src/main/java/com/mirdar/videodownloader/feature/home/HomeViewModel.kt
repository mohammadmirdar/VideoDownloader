package com.mirdar.videodownloader.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.adivery.sdk.Adivery
import com.adivery.sdk.AdiveryListener
import com.mirdar.videodownloader.R
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.AttachDownloadUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.CreateFileDownloadUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.DownloadUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.GetDownloadStateUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.GetDownloadStatusUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.RetryDownloadUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model.HomeUiEvents
import com.mirdar.videodownloader.com.mirdar.videodownloader.util.com.mirdar.videodownloader.util.DownloaderSnackUiEvent
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import com.mirdar.videodownloader.domain.home.usecase.GetVideoInfoUseCase
import com.mirdar.videodownloader.feature.home.model.HomeUiState
import com.mirdar.videodownloader.feature.home.model.VideoInfo
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.util.GetString
import com.mirdar.videodownloader.util.NetworkState
import com.mirdar.videodownloader.util.State
import com.mirdar.videodownloader.util.StringResourceProvider
import com.mirdar.videodownloader.util.toError
import com.mirdar.videodownloader.util.toSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appConfig: AppConfig,
    private val requestVideoInfo: GetVideoInfoUseCase,
    private val createFileDownload: CreateFileDownloadUseCase,
    private val download: DownloadUseCase,
    private val getDownloadStatus: GetDownloadStatusUseCase,
    private val getDownloadState: GetDownloadStateUseCase,
    private val getString: GetString,
    private val retry: RetryDownloadUseCase,
    private val attachDownload: AttachDownloadUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _state = MutableStateFlow<NetworkState<HomeUiState>>(State.Loading())
    val state = _state.asStateFlow()

    private val _event = Channel<HomeUiEvents>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    var currentRequestedVideo: String = ""

    init {
        listenOnAdivery()
        observeDownloadStatus()
        observeDownloadState()
        attachDownload()
    }

    private fun observeDownloadState() {
        getDownloadState()
            .onEach { state ->
                _state.update {
                    HomeUiState(
                        downloads = state.items
                    ).toSuccess()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeDownloadStatus() {
        getDownloadStatus()
            .distinctUntilChanged()
            .onEach { (id, status) ->
                when (status) {
                    DownloadStatus.Delete -> _event.trySend(HomeUiEvents.HideToast)
                    else -> {
                        val message =
                            status.messageId?.let { resId ->
                                stringResourceProvider.getString(
                                    resourceId = resId
                                )
                            }.orEmpty()
                        val actionLabel =
                            status.actionLabelId?.let { resId ->
                                stringResourceProvider.getString(
                                    resourceId = resId
                                )
                            }
                        if (message.isNotEmpty()) {
                            val snackBar = DownloaderSnackUiEvent.Snackbar(
                                message = message,
                                isError = status.isError,
                                callAction = { retry.invoke(id = id) },
                                actionLabel = actionLabel,
                                withDismissAction = true,
                                icon = status.iconId,
                            )
                            _event.trySend(HomeUiEvents.Toast(snackBar = snackBar))
                        }
                    }
                }
            }
            .launchIn(viewModelScope)

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
        getVideoInfo(url = videoUrl)

        currentRequestedVideo = videoUrl
    }

    private fun onRightVideoInfo(videoInfoModel: VideoInfoModel) {
        onStartDownload(video = videoInfoModel)

        _state.value = HomeUiState(
            videoInfo = VideoInfo(
                description = videoInfoModel.description,
                directUrl = videoInfoModel.directUrl,
                thumbnail = videoInfoModel.thumbnail,
                title = videoInfoModel.title
            )
        ).toSuccess()
    }

    private fun onLeftVideoInfo(callError: CallError) {
        _state.value = callError.toError()
    }

    fun onStartDownload(video: VideoInfoModel) {
        viewModelScope.launch {
            val fileName = "/${video.title}-${System.currentTimeMillis()}.mp4"
            val uri = createFileDownload(fileName = fileName)
            val fileUri = uri?.toString().orEmpty()
            val isStarted = download(
                item = DownloadItem(
                    fileUri = fileUri,
                    title = video.title,
                    poster = video.thumbnail,
                    url = video.directUrl,
                    createTime = System.currentTimeMillis()
                )
            )
            val message = when (isStarted) {
                true -> getString(R.string.download_started)
                else -> getString(R.string.download_failed)
            }
            val actionLabel = when (isStarted) {
                true -> getString(R.string.go_to_download)
                else -> null
            }
            _event.trySend(
                HomeUiEvents.Message(
                    snackBar = DownloaderSnackUiEvent.Snackbar(
                        message = message,
                        icon = R.drawable.ic_download_shorts,
                        isError = !isStarted,
                        actionLabel = actionLabel,
                        callAction = { if (isStarted) _event.trySend(HomeUiEvents.NavigateToDownload) }
                    )
                )
            )
        }
    }
}