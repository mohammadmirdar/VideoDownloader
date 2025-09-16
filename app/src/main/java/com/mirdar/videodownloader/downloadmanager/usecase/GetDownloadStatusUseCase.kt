package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadStatusUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(): Flow<Pair<Int, DownloadStatus>> {
        return downloadRepository.getDownloadStatus()
    }
}