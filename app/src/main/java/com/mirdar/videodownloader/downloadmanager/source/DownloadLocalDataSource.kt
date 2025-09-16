package com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.source

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.di.DOWNLOAD_MOSHI_ADAPTER
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.di.DefaultDispatcher
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.di.IoDispatcher
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.listener.DownloadListener
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.mapper.DownloadMapper
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadItem
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadStatus
import com.mirdar.videodownloader.com.mirdar.videodownloader.downloadmanager.model.DownloadsState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Request
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DownloadLocalDataSource @Inject constructor(
    private val fetch: Fetch,
    @ApplicationContext private val context: Context,
    @Named(DOWNLOAD_MOSHI_ADAPTER) moshi: Moshi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    private val adapter: JsonAdapter<DownloadItem> = moshi.adapter(DownloadItem::class.java)

    private val _state = MutableStateFlow(DownloadsState())
    val state: StateFlow<DownloadsState> = _state.asStateFlow()

    private val _event = Channel<Pair<Int, DownloadStatus>>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    private val listener = DownloadListener(
        onUpdate = { download, waiting ->
            updateDownload(download = download, waiting = waiting)
        },
        onFinalize = { uri ->
            if (uri != null) finalizeDownload(uri = uri)
        }
    )

    init {
        refresh()
    }

    private fun refresh() {
        fetch.getDownloads { downloads ->
            scope.launch {
                val newItems = downloads.mapNotNull { download ->
                    withContext(defaultDispatcher) {
                        DownloadMapper.mapDownload(
                            download = download,
                            adapter = adapter
                        )
                    }
                }.filterNot { item -> item.status == DownloadStatus.Delete }
                    .sortedByDescending { item -> item.createTime }.toImmutableList()
                _state.update { it.copy(items = newItems, lastUpdate = System.currentTimeMillis()) }
            }
        }
    }

    suspend fun download(item: DownloadItem): Boolean = suspendCoroutine { continuation ->
        scope.launch {
            val request = withContext(defaultDispatcher) {
                Request(url = item.url, fileUri = item.fileUri.toUri()).apply {
                    tag = adapter.toJson(item)
                }
            }
            fetch.enqueue(
                request = request,
                func = { continuation.resume(true) },
                func2 = { continuation.resume(false) })
        }
    }

    fun delete(id: Int) {
        fetch.delete(id = id, func = { download -> updateDownload(download = download) }, func2 = {
            forceDelete(id = id)
        })
    }

    fun pause(id: Int) = fetch.pause(id = id)

    fun resume(id: Int) = fetch.resume(id = id)

    fun retry(id: Int) {
        fetch.retry(id = id, func = { download -> updateDownload(download = download) })
    }

    fun cancel(id: Int) {
        fetch.cancel(id = id, func = { download ->
            delete(id = id)
            updateDownload(download = download)
        }, func2 = {
            forceDelete(id = id)
        })
    }

    fun attach() {
        fetch.addListener(listener)
    }

    fun detach() {
        fetch.removeListener(listener)
    }

    private fun forceDelete(id: Int) {
        _state.update { state ->
            state.copy(
                items = state.items.filterNot { item -> item.id == id }
                    .sortedByDescending { it.createTime }
                    .toImmutableList(),
                lastUpdate = System.currentTimeMillis()
            )
        }
    }

    private fun updateDownload(download: Download, waiting: Boolean = false) {
        scope.launch {
            val newItem = withContext(defaultDispatcher) {
                DownloadMapper.mapDownload(
                    download = download,
                    adapter = adapter,
                    isWaitingForNetwork = waiting
                )
            } ?: return@launch

            _state.update { state ->
                state.copy(
                    items = state.items
                        .filterNot { oldItem -> oldItem.id == newItem.id }
                        .plus(newItem)
                        .filterNot { item -> item.status == DownloadStatus.Delete }
                        .map { item -> item.copy(progress = item.progress.coerceAtLeast(0)) }
                        .sortedByDescending { item -> item.createTime }
                        .toImmutableList(),
                    lastUpdate = System.currentTimeMillis()
                )
            }

            _event.trySend(newItem.id to newItem.status)
        }
    }

    fun createFileDownload(fileName: String): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }
            val collection =
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            context.contentResolver.insert(collection, values)
        } else {
            val moviesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            if (!moviesDir.exists()) moviesDir.mkdirs()
            val file = File(moviesDir, fileName)
            val values = ContentValues().apply {
                put(MediaStore.Video.Media.DATA, file.absolutePath)
                put(MediaStore.Video.Media.TITLE, fileName)
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            }
            context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    private fun finalizeDownload(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Video.Media.IS_PENDING, 0)
            }
            context.contentResolver.update(uri, values, null, null)
        }
    }
}
