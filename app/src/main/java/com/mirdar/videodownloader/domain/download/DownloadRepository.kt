package com.mirdar.videodownloader.domain.download

import com.mirdar.videodownloader.data.download.remote.model.DownloadRequest
import com.mirdar.videodownloader.data.download.remote.model.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    /** Start or resume a download and emit status updates. */
    fun download(request: DownloadRequest, videoType: VideoType): Flow<DownloadStatus>


    /** Cancel an in-flight download by id. */
    fun cancel(id: String)
}