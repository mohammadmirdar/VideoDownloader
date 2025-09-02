package com.mirdar.videodownloader.domain.download.usecase

import com.mirdar.videodownloader.data.download.remote.model.DownloadRequest
import com.mirdar.videodownloader.data.download.remote.model.DownloadStatus
import com.mirdar.videodownloader.domain.download.DownloadRepository
import com.mirdar.videodownloader.domain.download.VideoType
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class StartDownloadUseCase @Inject constructor(
    private val repository: DownloadRepository
) {
    operator fun invoke(request: DownloadRequest): Flow<DownloadStatus> {
        val type = if (request.url.contains(".m3u8")) VideoType.Hls else VideoType.Progressive
        return repository.download(request = request, videoType = type)
    }
}