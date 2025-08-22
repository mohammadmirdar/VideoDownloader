package com.mirdar.videodownloader.data.home

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.data.home.model.VideoInfoRequest
import com.mirdar.videodownloader.data.home.model.VideoInfoResponse
import retrofit2.http.GET

interface HomeService {

    @GET("video-info")
    suspend fun getVideoInfo(videoInfoRequest: VideoInfoRequest): Either<CallError, VideoInfoResponse>

}