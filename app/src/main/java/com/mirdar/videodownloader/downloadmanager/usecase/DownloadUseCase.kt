package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase

import android.content.Context
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository.DownloadRepository
import com.tonyodev.fetch2core.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository,
    @ApplicationContext private val context: Context,
) {
    suspend operator fun invoke(item: DownloadItem): Boolean {
        if (!context.isNetworkAvailable()) return false
        return downloadRepository.download(item = item)
    }
}