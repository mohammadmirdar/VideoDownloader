package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history.model

import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model.HomeError

sealed class HistoryError {
    data class NetworkError(val callError: CallError): HistoryError()

    data object None: HistoryError()
}