package com.mirdar.videodownloader.domain.download.usecase

import com.mirdar.videodownloader.data.download.model.DownloadId
import com.mirdar.videodownloader.domain.download.DownloadRepository
import jakarta.inject.Inject

class CancelDownloadUseCase @Inject constructor(
    private val repository: DownloadRepository
) {
    operator fun invoke(id: DownloadId) = repository.cancel(id)
}