package com.mirdar.videodownloader.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mirdar.videodownloader.data.download.model.DownloadId
import com.mirdar.videodownloader.data.download.model.DownloadRequest
import com.mirdar.videodownloader.data.download.model.DownloadStatus
import com.mirdar.videodownloader.domain.download.usecase.CancelDownloadUseCase
import com.mirdar.videodownloader.domain.download.usecase.StartDownloadUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {


    @Inject
    lateinit var startDownload: StartDownloadUseCase

    @Inject
    lateinit var cancelDownload: CancelDownloadUseCase


    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var activeJob: Job? = null

    companion object {
        private const val CHANNEL_ID = "download_channel"
        private const val NOTIF_ID = 42

        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_DEST = "extra_dest"
        const val EXTRA_RESUME = "extra_resume"

        fun start(context: Context, request: DownloadRequest) {
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(EXTRA_ID, request.id.value)
                putExtra(EXTRA_URL, request.url)
                putExtra(EXTRA_DEST, request.destination)
                putExtra(EXTRA_RESUME, request.resumeIfPossible)
            }
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    fun buildNotification(
        context: Context,
        title: String = "Downloading",
        percent: Int = 0,
        indeterminate: Boolean = false
    ): Notification {
        val nm =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel(
                CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            )
            nm.createNotificationChannel(ch)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        if (indeterminate) {
            builder.setProgress(0, 0, true)
        } else {
            builder.setProgress(100, percent, false)
        }

        return builder.build()
    }


    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val id = intent?.getStringExtra(EXTRA_ID) ?: return START_NOT_STICKY
        val url = intent.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
        val dest = intent.getParcelableExtra<android.net.Uri>(EXTRA_DEST) ?: return START_NOT_STICKY
        val resume = intent.getBooleanExtra(EXTRA_RESUME, true)


        startForeground(NOTIF_ID, buildNotification(this, title = "Preparing…"))


        val request = DownloadRequest(DownloadId(id), url, dest, resumeIfPossible = resume)
        activeJob?.cancel()
        activeJob = serviceScope.launch {
            startDownload(request).collectLatest { status ->
                when (status) {
                    is DownloadStatus.Started -> updateNotification(
                        "Downloading...",
                        status.totalBytes?.let { 0 } ?: 0,
                        indeterminate = status.totalBytes == null
                    )

                    is DownloadStatus.Progress -> updateNotification(
                        "Downloading...",
                        status.progress.percent ?: 0,
                        indeterminate = status.progress.percent == null
                    )

                    is DownloadStatus.Completed -> {
                        updateNotification("Completed", 100, indeterminate = false)
                        stopSelf()
                    }

                    is DownloadStatus.Failed -> {
                        updateNotification(
                            "Failed: ${status.error.message}",
                            0,
                            indeterminate = true
                        )
                        stopSelf()
                    }

                    is DownloadStatus.Cancelled -> {
                        updateNotification("Cancelled", 0, indeterminate = true)
                        stopSelf()
                    }
                }
            }
        }


        return START_REDELIVER_INTENT
    }


    override fun onDestroy() {
        super.onDestroy()
        activeJob?.cancel()
    }


    private fun updateNotification(text: String, percent: Int, indeterminate: Boolean) {
        val notif = buildNotification(this, text, percent, indeterminate)
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIF_ID, notif)
    }
}