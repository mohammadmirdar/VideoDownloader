package com.mirdar.videodownloader.feature.home

import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
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
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model.HomeError
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model.HomeUiEvents
import com.mirdar.videodownloader.com.mirdar.videodownloader.util.com.mirdar.videodownloader.util.DownloaderSnackUiEvent
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import com.mirdar.videodownloader.domain.home.usecase.GetVideoInfoUseCase
import com.mirdar.videodownloader.feature.home.model.HomeUiState
import com.mirdar.videodownloader.feature.home.model.VideoInfo
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.util.GetString
import com.mirdar.videodownloader.util.State
import com.mirdar.videodownloader.util.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
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

typealias HomeState<T> = State<Unit, T, HomeError>

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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

    private val _state = MutableStateFlow(HomeUiState())
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
                            .take(3)
                            .filter { item -> context.uriExists(item.fileUri.toUri()) }
                            .toImmutableList()
                    )
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
            override fun onAppOpenAdLoaded(placementId: String) {
                _state.update { it.copy(copiedText = "") }
            }

            override fun onRewardedAdClosed(placementId: String, isRewarded: Boolean) {
                _state.update { it.copy(isLoading = true) }
                if (isRewarded) {
                    getVideoInfo(url = currentRequestedVideo)
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
        if (videoUrl.isNotEmpty()) {
            Adivery.showAd(appConfig.adiveryRewardId)
            currentRequestedVideo = videoUrl
        } else {
            _state.update { it.copy(homeError = HomeError.EmptyInput(message = context.getString(R.string.input_is_empty))) }
        }
    }

    private fun onRightVideoInfo(videoInfoModel: VideoInfoModel) {
        onStartDownload(video = videoInfoModel)

        _state.update {
            it.copy(
                videoInfo = VideoInfo(
                    description = videoInfoModel.description,
                    directUrl = videoInfoModel.directUrl,
                    thumbnail = videoInfoModel.thumbnail,
                    title = videoInfoModel.title
                ),
                isLoading = false
            )
        }
    }

    private fun onLeftVideoInfo(callError: CallError) {
        _state.update { it.copy(homeError = HomeError.NetworkError(callError = callError), isLoading = false) }
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

    fun onPasteClicked() {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val item = clipboard.primaryClip?.getItemAt(0)
        val text = item?.text.toString()

        if (text.isNotEmpty()) {
            _state.update {
                it.copy(copiedText = text)
            }
        }
    }

    fun clearInputError() {
        _state.update { it.copy(homeError = HomeError.None) }
    }

    fun onViewAllClicked() {
        _event.trySend(HomeUiEvents.NavigateToHistory)
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

fun Context.uriExists(uri: Uri): Boolean {
    return try {
        contentResolver.query(uri, arrayOf(MediaStore.MediaColumns._ID), null, null, null)
            ?.use { cursor ->
                cursor.moveToFirst() // true if at least one row exists
            } ?: false
    } catch (e: Exception) {
        false
    }
}