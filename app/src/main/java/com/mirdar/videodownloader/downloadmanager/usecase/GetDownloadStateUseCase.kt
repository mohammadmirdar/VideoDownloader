package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadsState
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadStateUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<DownloadsState> {
        return downloadRepository.getDownloadState()
    }
}