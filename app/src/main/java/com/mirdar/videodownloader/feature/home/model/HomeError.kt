package com.mirdar.videodownloader.com.mirdar.videodownloader.feature.home.model

import arrow.retrofit.adapter.either.networkhandling.CallError

sealed class HomeError {
    data class NetworkError(val callError: CallError): HomeError()
    data class EmptyInput(val message: String): HomeError()
    data object None: HomeError()
}