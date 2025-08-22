package com.mirdar.videodownloader.data.home

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.data.home.model.VideoInfoRequest
import com.mirdar.videodownloader.data.home.model.VideoInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeService {

    @POST("video-info")
    suspend fun getVideoInfo(@Body videoInfoRequest: VideoInfoRequest): Either<CallError, VideoInfoResponse>

}