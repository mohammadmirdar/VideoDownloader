package com.mirdar.videodownloader.domain.home

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel

interface HomeRepository {

    suspend fun getVideoInfo(url: String): Either<CallError, VideoInfoModel>
}