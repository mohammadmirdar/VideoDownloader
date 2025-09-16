package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.mirdar.designsystem.util.farsilize
import com.mirdar.videodownloader.R
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.receiver.DownloadActionNotificationReceiver
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.receiver.DownloadActionNotificationReceiver.Companion.DOWNLOAD_ID
import com.tonyodev.fetch2.DefaultFetchNotificationManager
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.DownloadNotification
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.util.DEFAULT_NOTIFICATION_TIMEOUT_AFTER_RESET
import kotlin.math.round

//TODO Needed Improve And Handle Action By Fetch Notification

class DownloadManagerNotification(
    private val context: Context
) : DefaultFetchNotificationManager(context = context) {

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var downloadNotification: DownloadNotification? = null
    private var notificationManager: NotificationManager? = null

    override fun getFetchInstanceForNamespace(namespace: String): Fetch = Fetch.getDefaultInstance()

    override fun updateNotification(
        notificationBuilder: NotificationCompat.Builder,
        downloadNotification: DownloadNotification,
        context: Context
    ) {
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_download_resumed)
            .setContentTitle(downloadNotification.title)
            .setContentText(getSubtitleText(context, downloadNotification))
            .setContentIntent(createDownloadPendingIntent())
            .setOngoing(downloadNotification.isOnGoingNotification)
            .setGroup(downloadNotification.groupId.toString())
            .setGroupSummary(false)

        if (downloadNotification.isFailed || downloadNotification.isCompleted) {
            val textRes =
                if (downloadNotification.isFailed) R.string.download_tryـagain_description else R.string.download_successfully
            notificationBuilder.setProgress(0, 0, false)
                .setContentText(context.getString(textRes))
        } else {
            val progressIndeterminate = downloadNotification.progressIndeterminate
            val maxProgress = if (downloadNotification.progressIndeterminate) 0 else 100
            val progress =
                if (downloadNotification.progress < 0) 0 else downloadNotification.progress
            notificationBuilder.setProgress(maxProgress, progress, progressIndeterminate)
        }

        when {
            downloadNotification.isQueued or downloadNotification.isDownloading -> {
                val downloaded =
                    round(downloadNotification.downloaded / (1024.0 * 1024.0)).toString()
                val total = round(downloadNotification.total / (1024.0 * 1024.0)).toString()
                val progress = downloadNotification.progress.toString()
                val contentTitle =
                    if (downloadNotification.isQueued) context.getString(R.string.download_queue) else context.getString(
                        R.string.download_downloading
                    )
                notificationBuilder.setContentTitle(contentTitle).setContentText(
                    context.getString(R.string.download_downloaded, downloaded, total, progress)
                        .farsilize()
                )
            }

            downloadNotification.isFailed -> {
                notificationBuilder.setContentText(context.getString(R.string.download_unsuccessfully))
                    .addAction(
                        cancelAction(downloadNotification = downloadNotification)
                    ).addAction(
                        retryAction(downloadNotification = downloadNotification)
                    )
            }

            else -> {
                notificationBuilder.setTimeoutAfter(DEFAULT_NOTIFICATION_TIMEOUT_AFTER_RESET)
            }
        }
        this.notificationBuilder = notificationBuilder
        this.downloadNotification = downloadNotification
    }


    override fun createNotificationChannels(
        context: Context,
        notificationManager: NotificationManager
    ) {
        this.notificationManager = notificationManager
        super.createNotificationChannels(context, notificationManager)
    }

    override fun postDownloadUpdate(download: Download): Boolean {
        updateDownloadNotification()
        return super.postDownloadUpdate(download)
    }

    private fun updateDownloadNotification() {
        downloadNotification?.takeIf { !it.isDeleted }?.let { item ->
            notificationManager?.notify(item.notificationId, notificationBuilder?.build())
        } ?: run {
            downloadNotification = null
        }
    }

    private fun cancelAction(
        downloadNotification: DownloadNotification,
    ): NotificationCompat.Action {
        return buildNotificationAction(
            title = context.getString(R.string.download_cancel),
            intent = createPendingIntent(
                Intent(
                    context,
                    DownloadActionNotificationReceiver::class.java
                ).apply {
                    putExtra(DOWNLOAD_ID, downloadNotification.notificationId)
                    action = DownloadActionNotificationReceiver.DownloadAction.DELETE.name
                })
        )
    }

    private fun retryAction(
        downloadNotification: DownloadNotification,
    ): NotificationCompat.Action {
        return buildNotificationAction(
            title = context.getString(R.string.download_tryـagain),
            intent = createPendingIntent(
                Intent(
                    context,
                    DownloadActionNotificationReceiver::class.java
                ).apply {
                    putExtra(DOWNLOAD_ID, downloadNotification.notificationId)
                    action = DownloadActionNotificationReceiver.DownloadAction.RETRY.name
                })
        )
    }

    private fun createPendingIntent(intent: Intent): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun buildNotificationAction(
        title: String,
        intent: PendingIntent,
    ): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            0,
            title,
            intent
        ).build()
    }

    private fun createDownloadPendingIntent(): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(
            context.packageName
        )?.apply {
            data = "https://shorts.aparat.com/download".toUri()
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
