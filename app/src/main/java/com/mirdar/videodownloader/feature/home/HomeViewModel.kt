package com.mirdar.videodownloader.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.adivery.sdk.Adivery
import com.adivery.sdk.AdiveryListener
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import com.mirdar.videodownloader.domain.home.usecase.GetVideoInfoUseCase
import com.mirdar.videodownloader.feature.home.model.HomeUiState
import com.mirdar.videodownloader.model.AppConfig
import com.mirdar.videodownloader.util.NetworkState
import com.mirdar.videodownloader.util.State
import com.mirdar.videodownloader.util.toError
import com.mirdar.videodownloader.util.toSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val requestVideoInfo: GetVideoInfoUseCase,
    private val appConfig: AppConfig
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
        Adivery.showAd(appConfig.adiveryRewardId)
        currentRequestedVideo = videoUrl
    }

    private fun onRightVideoInfo(videoInfoModel: VideoInfoModel) {
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
}