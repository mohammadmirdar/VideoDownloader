package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import javax.inject.Inject

class PauseDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(id: Int) {
        downloadRepository.pause(id = id)
    }
}