package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.repository

import android.net.Uri
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadsState
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.source.DownloadLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DownloadRepository {
    suspend fun download(item: DownloadItem): Boolean
    fun createFileDownload(fileName: String): Uri?
    fun getDownloadState(): Flow<DownloadsState>
    fun getDownloadStatus(): Flow<Pair<Int, DownloadStatus>>
    fun delete(id: Int)
    fun pause(id: Int)
    fun resume(id: Int)
    fun retry(id: Int)
    fun cancel(id: Int)
    fun attach()
    fun detach()
}

class DownloadRepositoryImpl @Inject constructor(
    private val local: DownloadLocalDataSource
) : DownloadRepository {

    override fun createFileDownload(fileName: String): Uri? {
        return local.createFileDownload(fileName = fileName)
    }

    override fun getDownloadState(): Flow<DownloadsState> {
        return local.state
    }

    override fun getDownloadStatus(): Flow<Pair<Int, DownloadStatus>> {
        return local.event
    }

    override fun delete(id: Int) {
        local.delete(id = id)
    }

    override suspend fun download(item: DownloadItem): Boolean {
        return local.download(item = item)
    }

    override fun pause(id: Int) {
        local.pause(id = id)
    }

    override fun resume(id: Int) {
        local.resume(id = id)
    }

    override fun retry(id: Int) {
        local.retry(id = id)
    }

    override fun cancel(id: Int) {
        local.cancel(id = id)
    }

    override fun attach() {
        local.attach()
    }

    override fun detach() {
        local.detach()
    }
}