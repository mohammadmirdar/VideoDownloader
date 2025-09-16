package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.DeleteDownloadUseCase
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.usecase.RetryDownloadUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadActionNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var retryDownload: RetryDownloadUseCase

    @Inject
    lateinit var deleteDownload: DeleteDownloadUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(DOWNLOAD_ID, -1)
        clearNotification(context = context, id = id)
        when (intent.action) {
            DownloadAction.RETRY.name -> retryDownload(id = id)
            DownloadAction.DELETE.name -> deleteDownload(id = id)
        }
    }

    private fun clearNotification(context: Context, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }

    enum class DownloadAction {
        RETRY, DELETE
    }

    companion object {
        const val DOWNLOAD_ID = "download_id"
    }
}
