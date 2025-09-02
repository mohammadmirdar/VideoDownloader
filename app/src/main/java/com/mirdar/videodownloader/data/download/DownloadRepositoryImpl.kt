package com.mirdar.videodownloader.data.download

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import com.mirdar.videodownloader.com.mirdar.videodownloader.data.download.DownloadApi
import com.mirdar.videodownloader.data.download.model.DownloadId
import com.mirdar.videodownloader.data.download.model.DownloadProgress
import com.mirdar.videodownloader.data.download.model.DownloadRequest
import com.mirdar.videodownloader.data.download.model.DownloadStatus
import com.mirdar.videodownloader.domain.download.DownloadRepository
import com.mirdar.videodownloader.domain.download.VideoType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: DownloadApi,
) : DownloadRepository {


    private val jobs = ConcurrentHashMap<DownloadId, Job>()


    override fun download(request: DownloadRequest, videoType: VideoType): Flow<DownloadStatus> {
        return when (videoType) {
            VideoType.Hls -> downloadHls(request = request)
            VideoType.Progressive -> downloadProgressive(request = request)
        }
    }

    private fun downloadProgressive(request: DownloadRequest) = callbackFlow {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            try {

                val cacheFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                    "video_${System.currentTimeMillis()}.mp4"
                )

                val response: Response<ResponseBody> = api.download(request.url)
                if (!response.isSuccessful) {
                    trySend(
                        DownloadStatus.Failed(
                            request.id,
                            IOException("HTTP ${response.code()}")
                        )
                    )
                    return@launch
                }
                val body = response.body() ?: run {
                    trySend(DownloadStatus.Failed(request.id, IOException("Empty body")))
                    return@launch
                }


                // Total length: for 206 Partial Content, content-range reports total; else content-length.
                val totalBytes: Long? = when (response.code()) {
                    206 -> parseTotalFromContentRange(response.headers()["Content-Range"]) // may be null
                    else -> body.contentLength().takeIf { it > 0L }
                }

                trySend(DownloadStatus.Started(request.id, totalBytes))

                val contentLength =
                    response.body()!!.contentLength() // might be -1 for chunked streams
                var bytesCopied = 0L

                response.body()!!.byteStream().use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                            bytesCopied += read

                            if (contentLength > 0) {
                                send(
                                    DownloadStatus.Progress(
                                        DownloadProgress(
                                            request.id,
                                            bytesCopied,
                                            contentLength
                                        )
                                    )
                                )
                            }
                        }
                    }
                }

            } catch (e: Exception) {

            }
        }

        awaitClose {
            cancel(request.id)
        }
    }

    private fun downloadHls(request: DownloadRequest): Flow<DownloadStatus> = flow {
        // 1. download m3u8 playlist
        val destination = File(request.destination.path.orEmpty())
        val response = api.download(request.url)
        if (response.body() != null) {
            val playlist = response.body()!!.string()
            val segmentUrls = parseM3u8(playlist, request.url)

            val segmentDir = File(context.cacheDir, "segments").apply { mkdirs() }
            val segmentFiles = mutableListOf<File>()

            segmentUrls.forEachIndexed { index, segmentUrl ->
                val segmentFile = File(segmentDir, "seg_$index.ts")
                val body = api.download(segmentUrl).body()?.byteStream()
                segmentFile.outputStream().use { output -> body!!.copyTo(output) }
                segmentFiles.add(segmentFile)
                emit(
                    DownloadStatus.Progress(
                        DownloadProgress(
                            id = request.id,
                            bytesDownloaded = (index + 1).toLong(),
                            totalBytes = segmentUrls.size.toLong()
                        )
                    )
                )
            }

            // 4. merge .ts into single .mp4
            mergeSegments(segmentFiles, request.id.value)
        }

        emit(
            DownloadStatus.Completed(
                id = request.id,
                uri = request.destination
            )
        )
    }

    private fun parseM3u8(playlist: String, baseUrl: String): List<String> {
        return playlist.lines()
            .filter { !it.startsWith("#") && it.isNotBlank() }
            .map { line -> if (line.startsWith("http")) line else baseUrl.substringBeforeLast("/") + "/" + line }
    }

    private fun mergeSegments(segments: List<File>, fileName: String) {
        val moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ?: throw IOException("Movies directory not available")

        val outputFile = File(moviesDir, "${fileName}.mp4")

        FileOutputStream(outputFile).use { output ->
            segments.forEach { segment ->
                FileInputStream(segment).use { input ->
                    input.copyTo(output)
                }
            }
        }
    }

    override fun cancel(id: DownloadId) {
        jobs[id]?.cancel()
    }


    private fun parseTotalFromContentRange(header: String?): Long? {
        // Example: bytes 200-1000/67589 -> total=67589
        if (header == null) return null
        return header.substringAfter('/')
            .toLongOrNull()
    }


    private fun currentBytes(resolver: ContentResolver, uri: Uri): Long {
        return try {
            resolver.openFileDescriptor(uri, "r")?.use { it.statSize.takeIf { s -> s >= 0 } ?: 0L }
                ?: 0L
        } catch (_: Throwable) {
            0L
        }
    }


    private fun writeStream(
        body: ResponseBody,
        pfd: ParcelFileDescriptor,
        startOffset: Long,
        totalBytes: Long?,
        onProgress: (downloadedSoFar: Long) -> Unit,
    ) {
        val buffer = ByteArray(DEFAULT_BUFFER)
        var totalRead = 0L
        var read: Int


        val fd: FileDescriptor = pfd.fileDescriptor
        FileOutputStream(fd).channel.use { channel ->

            if (startOffset > 0) channel.position(startOffset)

            body.byteStream().use { input ->
                while (true) {
                    read = input.read(buffer)
                    if (read == -1) break
                    channel.write(ByteBuffer.wrap(buffer, 0, read))
                    totalRead += read
                    onProgress(totalRead)
                }
                channel.force(true)
            }
        }
    }


    companion object {
        const val DEFAULT_BUFFER = 8 * 1024
    }
}