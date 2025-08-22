package com.mirdar.videodownloader.domain.home.usecase

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import com.mirdar.videodownloader.domain.home.HomeRepository
import com.mirdar.videodownloader.domain.home.model.VideoInfoModel

class GetVideoInfoUseCase(private val homeRepository: HomeRepository) {

    suspend operator fun invoke(url: String): Either<CallError, VideoInfoModel> =
        homeRepository.getVideoInfo(url)
}