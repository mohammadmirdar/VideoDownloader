package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.adapter

import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

class DownloadStatusJsonAdapter {
    @FromJson
    fun fromJson(json: DownloadStatusJson): DownloadStatus = when (json.type) {
        DownloadStatus.Completed::class.java.simpleName -> DownloadStatus.Completed
        DownloadStatus.Delete::class.java.simpleName -> DownloadStatus.Delete
        DownloadStatus.Downloading::class.java.simpleName -> DownloadStatus.Downloading
        DownloadStatus.Failed::class.java.simpleName -> DownloadStatus.Failed
        DownloadStatus.Init::class.java.simpleName -> DownloadStatus.Init
        DownloadStatus.Network::class.java.simpleName -> DownloadStatus.Network
        DownloadStatus.Paused::class.java.simpleName -> DownloadStatus.Paused
        DownloadStatus.Queued::class.java.simpleName -> DownloadStatus.Queued
        DownloadStatus.Resume::class.java.simpleName -> DownloadStatus.Resume
        else -> throw IllegalArgumentException("Unknown action type: ${json.type}")
    }

    @ToJson
    fun toJson(action: DownloadStatus): DownloadStatusJson = DownloadStatusJson(
        type = when (action) {
            DownloadStatus.Completed -> DownloadStatus.Completed::class.java.simpleName
            DownloadStatus.Delete -> DownloadStatus.Delete::class.java.simpleName
            DownloadStatus.Downloading -> DownloadStatus.Downloading::class.java.simpleName
            DownloadStatus.Failed -> DownloadStatus.Failed::class.java.simpleName
            DownloadStatus.Init -> DownloadStatus.Init::class.java.simpleName
            DownloadStatus.Network -> DownloadStatus.Network::class.java.simpleName
            DownloadStatus.Paused -> DownloadStatus.Paused::class.java.simpleName
            DownloadStatus.Queued -> DownloadStatus.Queued::class.java.simpleName
            DownloadStatus.Resume -> DownloadStatus.Resume::class.java.simpleName
        }
    )
}

@JsonClass(generateAdapter = true)
data class DownloadStatusJson(val type: String)
