package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase

import android.net.Uri
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import javax.inject.Inject

class CreateFileDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(fileName: String): Uri? {
        return downloadRepository.createFileDownload(fileName = fileName)
    }
}