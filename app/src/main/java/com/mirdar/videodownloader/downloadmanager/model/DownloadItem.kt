package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model

import android.os.Environment
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DownloadItem(
    val id: Int = -1,
    val status: DownloadStatus = DownloadStatus.Init,
    val progress: Int = 0,
    val url: String = "",
    val fileUri: String = "",
    val title: String = "",
    val quality: String = "",
    val duration: String = "",
    val channel: String = "",
    val channelPoster: String = "",
    val poster: String = "",
    val createTime: Long = -1,
    val lastUpdateTime: Long = -1
) {
    companion object {
        fun toFileUri(path: String) =
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                .toString() + path)
    }
}
