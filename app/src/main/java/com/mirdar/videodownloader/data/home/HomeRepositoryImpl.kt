package com.mirdar.videodownloader.data.home

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.data.home.model.VideoInfoRequest
import com.mirdar.videodownloader.data.home.model.toDomain
import com.mirdar.videodownloader.domain.home.HomeRepository
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {
    override suspend fun getVideoInfo(url: String): Either<CallError, VideoInfoModel> =
        homeService.getVideoInfo(VideoInfoRequest(url = url)).map { it.toDomain() }
}