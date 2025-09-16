package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model

import com.mirdar.videodownloader.R


sealed class DownloadStatus(
    val type: String,
    val iconId: Int?,
    val messageId: Int?,
    val isError: Boolean,
    val actionLabelId: Int?,
) {
    data object Init : DownloadStatus(
        iconId = null,
        isError = false,
        messageId = null,
        actionLabelId = null,
        type = Init::class.java.name,
    )

    data object Queued : DownloadStatus(
        isError = false,
        actionLabelId = null,
        messageId = R.string.download_queued,
        iconId = R.drawable.ic_download_resumed,
        type = Queued::class.java.name,
    )

    data object Resume : DownloadStatus(
        isError = false,
        actionLabelId = null,
        iconId = R.drawable.ic_download_resumed,
        messageId = R.string.download_resume,
        type = Resume::class.java.name,
    )

    data object Paused : DownloadStatus(
        isError = false,
        actionLabelId = null,
        type = Paused::class.java.name,
        messageId = R.string.download_stoped,
        iconId = R.drawable.ic_download_pause,
    )

    data object Downloading : DownloadStatus(
        isError = false,
        messageId = null,
        actionLabelId = null,
        type = Downloading::class.java.name,
        iconId = null,
    )

    data object Network : DownloadStatus(
        isError = false,
        actionLabelId = null,
        type = Network::class.java.name,
        messageId = R.string.download_network_connection,
        iconId = R.drawable.ic_download_network,
    )

    data object Failed : DownloadStatus(
        isError = true,
        type = Failed::class.java.name,
        messageId = R.string.download_unsuccessfully,
        iconId = R.drawable.ic_download_failed,
        actionLabelId = R.string.download_tryـagain,
    )

    data object Completed : DownloadStatus(
        isError = false,
        actionLabelId = null,
        messageId = R.string.download_complete,
        iconId = R.drawable.ic_download_complete,
        type = Completed::class.java.name,
    )

    data object Delete : DownloadStatus(
        iconId = null,
        isError = false,
        messageId = null,
        actionLabelId = null,
        type = Delete::class.java.name,
    )
}

